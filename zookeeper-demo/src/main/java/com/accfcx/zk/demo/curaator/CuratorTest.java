package com.accfcx.zk.demo.curaator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.api.UnhandledErrorListener;
import org.apache.curator.retry.RetryOneTime;

/**
 * @author accfcx
 * @desc
 */
public class CuratorTest {

    CuratorListener listener = new CuratorListener() {
        @Override
        public void eventReceived(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
            try{
                switch (curatorEvent.getType()) {
                    case CREATE:
                        System.out.println("create");
                        break;
                    case DELETE:
                        System.out.println("del");
                    case CHILDREN:
                        System.out.println("children");
                        break;
                    case WATCHED:
                        System.out.println("watch");
                        break;
                    default:
                }
            } catch (Exception e) {
                e.printStackTrace();
                curatorFramework.close();
            }

        }
    };

    UnhandledErrorListener unhandledErrorListener = new UnhandledErrorListener() {
        @Override
        public void unhandledError(String s, Throwable throwable) {
            throwable.printStackTrace();
        }
    };

    RetryPolicy retryPolicy = new RetryOneTime(500);
    CuratorFramework client = CuratorFrameworkFactory.newClient("10.96.93.36:2181,10.96.96.72:2181,10.96.97.75:2181", retryPolicy);

    {
        client.getCuratorListenable().addListener(listener);
        client.getUnhandledErrorListenable().addListener(unhandledErrorListener);

        new Thread(() -> {

        }).start();
    }


}
