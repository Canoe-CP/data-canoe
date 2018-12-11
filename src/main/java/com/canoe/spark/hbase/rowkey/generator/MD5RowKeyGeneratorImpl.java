package com.canoe.spark.hbase.rowkey.generator;

import com.canoe.spark.hbase.rowkey.RowkeyGenerator;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * program: data-canoe
 * author:   Canoe
 * create: 2018-12-05 16:17
 * md5
 */
public class MD5RowKeyGeneratorImpl implements RowkeyGenerator<String> {

    private long currentId = 1 ;

    @Override
    public byte[] generator(String s) {
        try{
            return DigestUtils.md5Hex(currentId + "" + System.currentTimeMillis()).getBytes();
        }finally {
            currentId ++ ;
        }
    }
}
