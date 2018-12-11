package com.canoe.spark.hbase.rowkey.generator;

import com.canoe.spark.hbase.rowkey.RowkeyGenerator;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * program: data-canoe
 * author:   Canoe
 * create: 2018-12-05 16:03
 *
 * hash rowkey
 */
public class HashRowRowKeyGeneratorImpl implements RowkeyGenerator<String> {
    private Logger LOG = LoggerFactory.getLogger(HashRowRowKeyGeneratorImpl.class);

    @Override
    public byte[] generator(String string) {//时间戳反转，再加上几个随机数字
        String rowKey = String.valueOf(System.currentTimeMillis());
        rowKey = new StringBuilder(rowKey).reverse().toString();
        String chars = "abcdefghijklmnopqrstuvwxyz";
        char char1 = chars.charAt((int)(Math.random()*26));
        char char2 = chars.charAt((int)(Math.random()*26));
        char char3 = chars.charAt((int)(Math.random()*26));
        rowKey = rowKey + char1 + char2 + char3 ;
        LOG.debug("rowKey : " + rowKey);
        return Bytes.toBytes(rowKey);

    }

}
