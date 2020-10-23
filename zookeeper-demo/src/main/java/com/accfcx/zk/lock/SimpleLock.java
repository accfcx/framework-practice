package com.accfcx.zk.lock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * @author accfcx
 * @desc 基于ZK的简易分布式锁实现
 */
public class SimpleLock implements Lock {

    private final CuratorFramework client;

    private final String LOCK_PATH = "/lock";

    public SimpleLock() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient("10.96.93.36:2181,10.96.96.72:2181,10.96.97.75:2181", retryPolicy);
        client.start();
    }

    @Override
    public boolean tryLock() throws Exception {
        // 检查 /lock节点是否存在
        try{
            client.checkExists().forPath(LOCK_PATH);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try{
            client.create().forPath(LOCK_PATH);
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    @Override
    public void lock() throws InterruptedException, BrokenBarrierException {
        // 检查是否有锁，有锁，阻塞当前线程；注册watcher-回调唤醒阻塞线程，再次尝试获取lock
        boolean continued = true;
//        final CyclicBarrier cyclicBarrier = new CyclicBarrier(1);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        while (continued) {
            Stat result = null;
            try{
                result = client.checkExists().usingWatcher(new CuratorWatcher() {
                    @Override
                    public void process(WatchedEvent watchedEvent) {
                        if(watchedEvent.getType() == Watcher.Event.EventType.NodeDeleted) {
                            System.out.println("/lock节点被删除，触发了watcher回调");
                            countDownLatch.countDown();
                        }
                    }
                }).forPath(LOCK_PATH);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result != null) {
                // 存在 /lock，阻塞
                System.out.println("节点存在，阻塞线程");
                countDownLatch.await();
            }
            // 不存在
            continued = false;
        }

        try{
            client.create().forPath(LOCK_PATH);
        } catch (Exception e) {
           e.printStackTrace();
        }

    }

    @Override
    public boolean tryRelease() {
        return false;
    }

    @Override
    public void release() {
        try{
            client.delete().forPath(LOCK_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public CuratorFramework getClient() {
        return client;
    }

    public static void main(String[] args) throws Exception {
        Lock lock = new SimpleLock();
        CuratorFramework client = lock.getClient();
        System.out.println(client.getChildren().forPath("/"));
//        boolean result = false;
        try{
//            result = lock.tryLock();
            lock.lock();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (!result) {
//            System.out.println("分布式锁被其它机器持有");
//        }
        System.out.println(client.getChildren().forPath("/"));
        System.out.println("doing doing doing ... for long time");
        Thread.sleep(10 * 1000);

        System.out.println("完成后释放分布式锁");
        lock.release();
    }
}
