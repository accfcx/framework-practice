package com.accfcx.api.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

import java.io.*;
import java.net.URI;

/**
 * @author accfcx
 * @desc
 */
public class FileTest1 {
    public static void main(String[] args) throws IOException {
//        FileTest1.testWrite("hdfs_write.txt");
//        FileTest1.testRead("hdfs_write.txt");
        FileTest1.testStatus();

    }

    public static void testRead(String fileName) throws IOException{
        String url = "hdfs://localhost:9000/" + fileName;
        Configuration configuration = new Configuration();

        FileSystem fs = FileSystem.get(URI.create(url), configuration);

        FSDataInputStream inputStream = null;
        try{
            inputStream  = fs.open(new Path(url));
            IOUtils.copyBytes(inputStream, System.out, 4096, false);

//            inputStream.seek(0);
//
//            IOUtils.copyBytes(inputStream, System.out, 4096, false);


        } finally {
            IOUtils.closeStream(inputStream);
        }
    }


    public static void testWrite(String fileName) throws IOException {
        String localStr = "/Users/accfcx/Project/demo/framework-practice/" + fileName;

        String dst = "hdfs://localhost:9000/hdfs_write.txt";

        Configuration configuration = new Configuration();

        InputStream inputStream = new BufferedInputStream(new FileInputStream(localStr));

        FileSystem fileSystem = FileSystem.get(URI.create(dst), configuration);
        OutputStream outputStream = fileSystem.create(new Path(dst), () -> System.out.println("写入文件中"));

        IOUtils.copyBytes(inputStream, outputStream, 4096, true);
    }

    public static void testStatus() throws IOException {
        String uri = "hdfs://localhost:9000/";
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(URI.create(uri), configuration);

        Path[] paths = new Path[1];
        paths[0] = new Path(uri);

        FileStatus[] statuses = fileSystem.listStatus(paths);
        Path[] listedPaths = FileUtil.stat2Paths(statuses);
        for (Path listedPath : listedPaths) {
            System.out.println(listedPath);
        }
    }

    public static void testPatternFilter() {

    }



}
