package com.accfcx.zk.demo.watcher;

import com.accfcx.zk.demo.base.ZKClient;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.List;

/**
 * @author accfcx
 * @desc
 * Callback - 对应异步调用：需要处理各种情况
 * Watcher - 表示对于节点的某种eventType感兴趣
 *
 * getChildren("/workers")时，使用异步回调方式发起请求，并同时注册/workers子节点
 *
 * callback - ConnectionLoss:再次获取/workers子节点；OK - 获取到子节点列表数据，做进一步处理；其它异常
 * watcher - 子节点列表发生变化时,master重新调用getChildren - 注意可以复用异步回调和注册接口
 */
public class WorkersWatcherTest {
    Watcher watcher = new Watcher() {
        @Override
        public void process(WatchedEvent watchedEvent) {
            if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                getWorkers();
            }
        }
    };

    AsyncCallback.ChildrenCallback childrenCallback = new AsyncCallback.ChildrenCallback() {
        @Override
        public void processResult(int i, String s, Object o, List<String> list) {
            switch (KeeperException.Code.get(i)) {
                case OK:
                    System.out.println("workers: " + list);
                    // master处理移除的task的assign以及恢复执行状态，并重新把tasks调度给调度给其它workers
                    break;
                case CONNECTIONLOSS:
                    getWorkers();
                    break;
                default:
                    System.out.println("getChildren error." + KeeperException.create(KeeperException.Code.get(i), s));
                    break;
            }
        }
    };


    void getWorkers() {
        // 异步回调、注册子节点
        ZKClient.zk.getChildren("/workers",
                watcher, childrenCallback,
                null);
    }
}
