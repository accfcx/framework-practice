package com.accfcx.zk.demo.base;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import java.util.Random;
import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

/**
 * @author accfcx
 * @desc 异常处理；没有使用到isMaster
 */
public class MasterTest {
    Random random = new Random(25);
    String serverId = Long.toString(random.nextLong());

    /**
     * 标记当前机器是否为master
     */
    static Boolean isLeader = false;

    /**
     *
     * @return true ： master exist
     */
    boolean checkIfMasterExist() {
        while (true) {
            try{
                Stat stat = new Stat();
                String masterId = new String(ZKClient.zk.getData("/mater", false, stat));
                isLeader = serverId.endsWith(masterId);
                return true;
            } catch (KeeperException.NoNodeException e) {
                return false;
            } catch (KeeperException | InterruptedException ignored) {}
        }
    }

    void runForMaster() throws InterruptedException {
        while(true) {
            /*
            当前机器尝试去创建 /master; 1 成功；2 或者已有master存在了；3 或者遇到异常
            针对 2，3需要再次检测是否当前
             */
            try{
                ZKClient.zk.create("/master", serverId.getBytes(),
                        OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                isLeader = true;
                break;
            } catch (KeeperException.NodeExistsException e) {
                isLeader = false;
                break;
            } catch (KeeperException ignored){}

            if (checkIfMasterExist()) {
                break;
            }
        }
    }

}
