package com.canoe.spark.hbase.rowkey.generator;

import com.canoe.spark.hbase.rowkey.RowkeyGenerator;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * program: data-canoe
 * author:   Canoe
 * create: 2018-12-05 15:52
 *
 * 使用文件名做RowKey
 */
public class FileRowKeyGeneratorImpl implements RowkeyGenerator<String> {
    private Logger LOG = LoggerFactory.getLogger(FileRowKeyGeneratorImpl.class);

    @Override
    public byte[] generator(String fileName) {
        //文件名+系统时间的rowley，并md5加密
        String rowKey = DigestUtils.md5Hex(fileName+""+System.currentTimeMillis());
        LOG.debug("RowKey : " + rowKey);

        //随机在生成的rowkey前面添加一个字符，避免造成热点
        String chars = "abcdef";
        char start = chars.charAt((int)(Math.random()*6));
        return Bytes.toBytes(start+rowKey);
    }
}
