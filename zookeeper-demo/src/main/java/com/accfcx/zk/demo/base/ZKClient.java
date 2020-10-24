package com.accfcx.zk.demo.base;

import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * @author accfcx
 * @desc 静态Zk客户端，用于其它测试
 */
public class ZKClient {
    public static ZooKeeper zk;

    static {
        try {
//            zk = new ZooKeeper("10.96.93.36:2181,10.96.96.72:2181,10.96.97.75:2181", 10000, null);
            zk = new ZooKeeper("127.0.0.1:2181", 10000, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
