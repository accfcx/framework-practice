package com.accfcx.zk.demo.api;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import java.util.Random;
import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

/**
 * @author accfcx
 * @desc
 */
public class MasterCallbackTest {
    boolean isLeader;
    AsyncCallback.StringCallback masterCreateCallback = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int i, String s, Object o, String s1) {
            System.out.println("异步回调调用 ");
            switch (KeeperException.Code.get(i)) {
                case  CONNECTIONLOSS:
                    checkIfMasterExist();
                    return;
                case OK:
                    isLeader = true;
                    break;
                default:
                    isLeader = false;
            }
            System.out.println("is leader : " + (isLeader ? "是" : "否"));
        }
    };

    AsyncCallback.DataCallback masterCheckCallback = new AsyncCallback.DataCallback() {
        @Override
        public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
            switch (KeeperException.Code.get(i)) {
                case CONNECTIONLOSS:
                    checkIfMasterExist();
                    return;
                case NONODE:
                    runForMaster();
                    return;
                default:
            }
        }
    };

    Random random = new Random(25);
    String serverId = Long.toString(random.nextLong());

    void runForMaster() {
        ZKClient.zk.create("/master", serverId.getBytes(), OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL, masterCreateCallback, null);
    }

    void checkIfMasterExist() {
        ZKClient.zk.getData("/master", false, masterCheckCallback, null);
    }
}
