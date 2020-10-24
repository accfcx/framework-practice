package com.accfcx.zk.demo.base;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Random;

/**
 * @author accfcx
 * @desc
 */
public class Master implements Watcher {

    private ZooKeeper zk;
    private final String serviceId = Integer.toString(new Random().nextInt());
    private boolean isLeader = false;

    private void startZk() throws IOException {
        String ips = "10.96.93.36:2181,10.96.96.72:2181,10.96.97.75:2181";
        zk = new ZooKeeper(ips, 10000, this);
    }

    private void stopZk() throws InterruptedException {
        zk.close();
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("event: " + watchedEvent.getState() + " " + watchedEvent.getPath());
    }

    public static void main(String[] args) throws Exception {
        Master master = new Master();
        master.startZk();

        master.runForMaster();

        System.out.println("serviceId: " + master.serviceId);

        if (master.isLeader) {
            System.out.println("master");
            Thread.sleep(1000*10);
        } else {
            System.out.println("not master");
        }

//        master.stopZk();
    }

    private void runForMaster() throws InterruptedException {
        while (true) {
            try {
                zk.create("/master", serviceId.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                isLeader = true;
                break;
            } catch (KeeperException.NodeExistsException e) {
                isLeader = false;
                e.printStackTrace();
                break;
            } catch (KeeperException e) {
                e.printStackTrace();
            }
            if (checkMaster()) {
                break;
            }
        }
    }

    private boolean checkMaster() throws InterruptedException {
        while (true) {
            try {
                Stat stat = new Stat();
                byte[] data = zk.getData("/master", false, stat);
                isLeader = new String(data).equals(serviceId);
                return true;
            } catch (KeeperException.NoNodeException e) {
                return false;
            } catch (KeeperException e) {
                e.printStackTrace();
            }
        }
    }
}
