package com.accfcx.zk.demo.base;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

/**
 * @author accfcx
 * @desc 考虑约束：at-most-once 和 at-least-once
 */
public class Client implements Watcher {
    private static final Logger log = LoggerFactory.getLogger(Client.class);
    @Override
    public void process(WatchedEvent watchedEvent) {

    }

    String addCommandToZk(String command) {
        while (true) {
            try {
                return ZKClient.zk.create("/tasks/task-",
                        command.getBytes(),
                        OPEN_ACL_UNSAFE,
                        CreateMode.EPHEMERAL_SEQUENTIAL);
            } catch (KeeperException.NodeExistsException e) {
                log.error("task already exist");
                throw new RuntimeException(e);
            } catch (KeeperException.ConnectionLossException ignored) {
            } catch (InterruptedException | KeeperException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Client client = new Client();
        System.out.println("created command: " + client.addCommandToZk("select * from user"));

        Thread.sleep(10000);
    }
}
