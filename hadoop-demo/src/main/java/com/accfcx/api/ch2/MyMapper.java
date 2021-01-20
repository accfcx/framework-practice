package com.accfcx.api.ch2;


import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author accfcx
 * @desc
 */
public class MyMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private static final int MISSING = 9999;

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        super.map(key, value, context);
        String line = value.toString();
        String year = line.substring(0, 4);
        int ariTemp;
        if (line.charAt(7) == '+') {
            ariTemp = Integer.parseInt(line.substring(8,10));
        } else {
            ariTemp = Integer.parseInt(line.substring(7, 9));
        }

        context.write(new Text(year), new IntWritable(ariTemp));
    }
}
