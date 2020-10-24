package com.accfcx.zk.demo.base;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.util.Date;

/**
 * @author accfcx
 * @desc get data of /master, /workers, /tasks, /assign/worker-X/task-Y
 * for master-worker
 *
 * 1 async 性能比 sync方式高很多
 * 2 master监听 workers, tasks, 以及完成assign
 * 3 worker维护 Queue<Task>
 * 4 Client注册Task的执行状态
 * 5 master容错
 * 6 worker容错
 */
public class AdminClient implements Watcher {
    @Override
    public void process(WatchedEvent watchedEvent) {

    }


    void listState() throws KeeperException {
        try {
            Stat stat = new Stat();
            byte[] masterData = ZKClient.zk.getData("/master", false, stat);
            System.out.println(new String(masterData) + " " + new Date(stat.getCtime()));
        } catch (KeeperException.NoNodeException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
