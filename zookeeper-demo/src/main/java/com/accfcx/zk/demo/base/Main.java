package com.accfcx.zk.demo.base;

/**
 * @author accfcx
 * @desc
 * 1 基于ZK锁，链式注册watch
 * 2 master - 监控workers， 处理queue<Task>，调度Task给Worker
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
//        // 同步方式获取 /master
//        MasterTest masterTest = new MasterTest();
//        masterTest.runForMaster();
//
//        if (MasterTest.isLeader) {
//            System.out.println("i'm leader");
//            Thread.sleep(30000);
//        } else {
//            System.out.println("not leader");
//        }

//        MasterCallbackTest masterCallbackTest = new MasterCallbackTest();
//        masterCallbackTest.checkIfMasterExist();
//
//        if (masterCallbackTest.isLeader) {
//            System.out.println("i'm leader ");
//            Thread.sleep(10000);
//        } else {
//            System.out.println("not leader");
//        }
        Worker worker= new Worker();
        worker.register();

        Thread.sleep(100000);
    }
}
