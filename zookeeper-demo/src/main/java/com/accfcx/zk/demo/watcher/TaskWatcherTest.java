package com.accfcx.zk.demo.watcher;

/**
 * @author accfcx
 * @desc
 * 异步获取/tasks 子节点，并注册Watcher，EventType.NodeChildChanged
 *
 * 获取到Task数据，并随机分配个某个worker，并且删除/tasks/task-Y - /assign/worker-X/task-Y
 */
public class TaskWatcherTest {
}
