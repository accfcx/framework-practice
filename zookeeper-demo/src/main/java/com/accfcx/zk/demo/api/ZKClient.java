package com.accfcx.zk.demo.api;

import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * @author accfcx
 * @desc 静态Zk客户端，用于其它测试
 */
public class ZKClient {
    static ZooKeeper zk;

    static {
        try {
            zk = new ZooKeeper("10.96.93.36:2181,10.96.96.72:2181,10.96.97.75:2181", 10000, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
