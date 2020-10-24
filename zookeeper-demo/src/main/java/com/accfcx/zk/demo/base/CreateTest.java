package com.accfcx.zk.demo.base;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * @author accfcx
 * @desc
 */
public class CreateTest {

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        String serverId = Integer.toHexString(100);
        ZooKeeper zooKeeper = ZKClient.zk;

        System.out.println(serverId);
//        zooKeeper.create("/master", serverId.getBytes(), OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        Stat stat = new Stat();
        System.out.println(new String(zooKeeper.getData("/master", null, stat)));
        System.out.println(stat);
        Thread.sleep(10000);
    }
}
