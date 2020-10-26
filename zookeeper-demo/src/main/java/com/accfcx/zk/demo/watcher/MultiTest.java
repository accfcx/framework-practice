package com.accfcx.zk.demo.watcher;

import com.accfcx.zk.demo.base.ZKClient;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Op;
import org.apache.zookeeper.OpResult;
import org.apache.zookeeper.Transaction;

import java.util.Arrays;
import java.util.List;

/**
 * @author accfcx
 * @desc
 */
public class MultiTest {

    static Op deleteNode(String path) {
        return Op.delete(path, -1);
    }

    static void transactionTest() throws KeeperException, InterruptedException {
        // transaction
//        Transaction transaction = new Transaction();
//        transaction.delete()
        Transaction transaction = ZKClient.zk.transaction();
        transaction.delete("/node1", -1);
        transaction.delete("/node2", -1);

        List<OpResult> results = transaction.commit();
        results.forEach(item->{
            System.out.println(item.getType());
        });
    }

    public static void main(String[] args) throws Exception{
//        List<OpResult> results = ZKClient.zk.multi(Arrays.asList(
//                MultiTest.deleteNode("/node1"),
//                MultiTest.deleteNode("/node2")
//        ));
//        results.forEach(System.out::println);
        MultiTest.transactionTest();
    }
}
