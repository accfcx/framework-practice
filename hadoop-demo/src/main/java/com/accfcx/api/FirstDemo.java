package com.accfcx.api;

import com.accfcx.api.ch2.MyMapper;
import com.accfcx.api.ch2.MyReducer;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapreduce.Job;

import java.io.IOException;

/**
 * @author accfcx
 * @desc map reduce first taste
 */
public class FirstDemo {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
//        Job job = Job.getInstance();


        JobConf jobConf = new JobConf();
        jobConf.setJobName("max temperature");
        jobConf.setJarByClass(FirstDemo.class);

        FileInputFormat.addInputPath(jobConf, new Path("/Users/accfcx/Project/demo/framework-practice/hadoop-demo/src/main/resources/year_temp"));
        FileOutputFormat.setOutputPath(jobConf, new Path("/Users/accfcx/Project/demo/framework-practice/hadoop-demo/src/main/resources/year_max_temp"));

//        jobConf.setMapperClass((Class<? extends Mapper>) MyMapper.class);
//        jobConf.setReducerClass((Class<? extends Reducer>) MyReducer.class);

        jobConf.setOutputKeyClass(Text.class);
        jobConf.setOutputValueClass(IntWritable.class);


        Job job = new Job(jobConf);
        System.out.println(job.waitForCompletion(true));

    }

}
