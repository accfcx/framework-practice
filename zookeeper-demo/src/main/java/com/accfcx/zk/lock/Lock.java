package com.accfcx.zk.lock;

import org.apache.curator.framework.CuratorFramework;

import java.util.concurrent.BrokenBarrierException;

/**
 * @author accfcx
 * @desc 分布式锁接口
 *
 *
 */
public interface Lock {
    /**
     * 尝试获取分布式锁-非阻塞
     *
     * @return 是否成功
     */
    boolean tryLock() throws Exception;

    /**
     * 获取分布式锁 - 阻塞
     * 最原始版本-不可重入，不支持公平性，不支持读写锁
     */
    void lock() throws InterruptedException, BrokenBarrierException;

    /**
     * 尝试释放分布式锁 - 非阻塞
     * @return 是否成功
     */
    boolean tryRelease();

    /**
     * 释放锁 - 阻塞
     */
    void release();

    CuratorFramework getClient();
}
