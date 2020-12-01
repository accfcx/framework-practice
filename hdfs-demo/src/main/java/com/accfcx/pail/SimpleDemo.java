package com.accfcx.pail;

import com.backtype.hadoop.pail.Pail;

/**
 * @author accfcx
 * @desc Big Data - Pail Client to operate HDFS
 */
public class SimpleDemo {

    public static void main(String[] args) throws Exception {
        simpleIO();
    }

    public static void simpleIO() throws Exception {
        Pail pail = Pail.create("/tmp/pail");
        Pail.TypedRecordOutputStream outputStream = pail.openWrite();

        outputStream.writeObject(new byte[] {1,2,3});
        outputStream.writeObject(new byte[] {1,2,3,4});
        outputStream.writeObject(new byte[] {1,2,3,4,5});
        outputStream.close();
    }
}
