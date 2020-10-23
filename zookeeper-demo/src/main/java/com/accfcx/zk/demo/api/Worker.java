package com.accfcx.zk.demo.api;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.*;

import java.util.Random;


/**
 * @author accfcx
 * @desc
 */
public class Worker implements Watcher {
    private static final Logger log = LoggerFactory.getLogger(Worker.class);

    Random random = new Random(100);
    String serverId = Integer.toHexString(random.nextInt());
    String status;

    @Override
    public void process(WatchedEvent watchedEvent) {

    }

    AsyncCallback.StringCallback stringCallback = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int i, String s, Object o, String s1) {
            switch (KeeperException.Code.get(i))   {
                case CONNECTIONLOSS:
                    register();
                    break;
                case OK:
                    log.info("register worker success: " + serverId);
                    break;
                case NODEEXISTS:
                    log.error("already exists");
                    break;
                default:
                    log.error("wrong: " + KeeperException.create(KeeperException.Code.get(i), s));
            }
        }
    };

    void register() {
        // 客户端保证 worker id 唯一
        ZKClient.zk.create("/workers/worker-" + serverId,
                "Idle".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL,
                stringCallback,
                null);
    }

    AsyncCallback.StatCallback statusUpdateCallback = new AsyncCallback.StatCallback() {
        @Override
        public void processResult(int i, String s, Object o, Stat stat) {
            switch (KeeperException.Code.get(i)) {
                case CONNECTIONLOSS:
                    updateWorkerStatus((String)o);
                case OK:

                default:
            }

        }
    };

    synchronized private void updateWorkerStatus(String status) {
        if (status.equals(this.status)) {
            ZKClient.zk.setData("/workers/" + serverId,
                    status.getBytes(),
                    -1,
                    statusUpdateCallback,
                    status);
        }
    }

    public void setStatus(String status) {
        this.status = status;
        updateWorkerStatus(status);
    }
}
