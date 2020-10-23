package com.accfcx.zk.demo.api;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;

/**
 * @author accfcx
 * @desc master-worker
 */
public class MasterWorkerAsyncTest {
    public void bootstrap() {

        createParent("/workers", new byte[0]);
        createParent("/tasks", new byte[0]);
        createParent("/assign", new byte[0]);
        createParent("/status", new byte[0]);
    }

    AsyncCallback.StringCallback createParentCallback  = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int i, String s, Object o, String s1) {
            switch (KeeperException.Code.get(i)) {
                case CONNECTIONLOSS:
                    createParent(s, (byte[]) o);
                    break;
                case OK:
                    System.out.println("parent created");
                    break;
                case NODEEXISTS:
                    System.out.println("parent is registered: "+ s);
                    break;
                default:
                    System.out.println("error. " + KeeperException.create(KeeperException.Code.get(i), s));
            }
        }
    };

    void createParent(String path, byte[] data) {
        ZKClient.zk.create(path,
                data,
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT,
                createParentCallback,
                data);
    }
}
