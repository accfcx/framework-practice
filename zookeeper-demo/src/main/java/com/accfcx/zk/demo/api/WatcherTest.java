package com.accfcx.zk.demo.api;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * @author accfcx
 * @desc daemon thread to maintain session
 */
public class WatcherTest implements Watcher {

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("client get event: " + watchedEvent);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String addresses = "10.96.93.36:2181,10.96.96.72:2181,10.96.97.75:2181";
        ZooKeeper zooKeeper = new ZooKeeper(addresses, 15000, new WatcherTest());

        Thread.sleep(10000);
//        zooKeeper.st
    }
}
