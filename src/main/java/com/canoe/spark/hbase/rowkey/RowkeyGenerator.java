package com.canoe.spark.hbase.rowkey;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 *
 * program: data-canoe
 * author:   Canoe
 * create: 2018-12-05 15:48
 *
 * RowKey生成器，RowKey设计三原则
 * <p>长度</p>
 * <p>唯一</p>
 * <p>散列</p>
 */
public interface RowkeyGenerator<T> extends Serializable {

    /**
     * 生成RowKey
     */
    byte[] generator(T t);
}
