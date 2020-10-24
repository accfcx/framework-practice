package com.accfcx.zk.demo.watcher;

import com.accfcx.zk.demo.base.ZKClient;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

/**
 * @author accfcx
 * @desc
 *  master watch
 *  exist
 *  ConnectionLoss - 再次检查是否存在master
 *  Ok - 不存在master
 *  NodeExists - 已存在master, 再次调用检查master exists方法并注册 - 保证一直注册
 *
 *  todo ZK-P76的选主逻辑
 */
public class MasterWatcherTest {
    private String status;
    /**
     *
     */
    AsyncCallback.StatCallback masterExistCallback = new AsyncCallback.StatCallback() {
        @Override
        public void processResult(int i, String s, Object o, Stat stat) {
            switch (KeeperException.Code.get(i)) {
                case CONNECTIONLOSS:
                    // retry
                    exists();
                    break;
                case OK:
                    if (stat == null) {
                        status = "running";
                        runForMaster();
                    }
                    break;
                default:
                    //什么情况？？
                    checkMaster();
                    break;

            }
        }
    };

    Watcher masterWatcher = new Watcher() {
        @Override
        public void process(WatchedEvent watchedEvent) {
            if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
                assert "/master".equals(watchedEvent.getPath());
                // master不存在了，尝试获取
                runForMaster();

            }
        }
    };

    void exists() {
        ZKClient.zk
                .exists("/master",
                        masterWatcher,
                        masterExistCallback,
                        null);
    }

    void runForMaster() {
        // 获取master
    }

    boolean checkMaster() {
        // 检查master是否存在
        return false;
    }
}
