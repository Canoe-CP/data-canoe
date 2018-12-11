package com.canoe.spark.hbase;

import com.canoe.common.Utils;
import com.canoe.spark.hbase.pool.HbasePool;
import com.google.common.collect.Lists;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.procedure2.util.StringUtils;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * program: data-canoe
 * author:   Canoe
 * create: 2018-12-05 16:24
 *
 * HBase 常用操作
 */
public class HBaseCompose {
    private static Logger log = LoggerFactory.getLogger(HBaseCompose.class);

    public static void closeAdmin(Admin admin) {
        Utils.close(admin);
    }

    /**
     * 创建表
     * @param tableName  表名
     * @param families   列族名
     * @throws Exception
     */
    public static void createTable(String tableName , String... families) throws Exception {
        createTable(tableName , families , null);
    }

    public static void createTable(String tableName , String family , byte[][] splitKeys) throws Exception {
        createTable(tableName, new String[]{family},splitKeys);
    }

    public static void createTable(String tableName , String[] families , byte[][] splitKeys) throws  Exception{
        Admin admin = null ;
        Connection conn = null ;
        try{
            conn = HbasePool.getInstance().borrowObject();
            admin = conn.getAdmin();
            TableName name = TableName.valueOf(tableName);
            if(admin.tableExists(name)){
                log.warn("table [{}] is exist",tableName);
            }else{
                HTableDescriptor tableDes = new HTableDescriptor(name);
                if(null != families){
                    for(String family : families){
                        HColumnDescriptor cloumDes = new HColumnDescriptor(family);
                        tableDes.addFamily(cloumDes);
                    }
                }
                admin.createTable(tableDes);
            }
        }catch(Exception e){
            log.error(e.toString());
        }finally{
            closeAdmin(admin);
            HbasePool.getInstance().returnObject(conn);
        }
    }

    /**
     * 查询所有的表名
     * @return 表集合
     * @throws Exception
     */
    public static List<String> showTables() throws Exception {
        Admin admin = null ;
        Connection conn = null ;
        List<String> tables = Lists.newArrayList();
        try{
            conn = HbasePool.getInstance().borrowObject();
            admin = conn.getAdmin();
            HTableDescriptor[] tableDecs = admin.listTables() ;
            for (HTableDescriptor tableDes : tableDecs){
                tables.add(tableDes.getNameAsString());
            }
            if(log.isDebugEnabled()){
                log.debug("tables : [{}]" , tables);
            }
        }catch(Exception e){
            log.error(e.toString());
        }finally{
            Utils.close(admin);
            HbasePool.getInstance().returnObject(conn);
        }
        return tables;
    }

    /**
     * 删除表
     * @param tableName    表名
     * @throws Exception
     */
    public static void deleteTable(String tableName) throws Exception {
        Admin admin = null ;
        Connection conn = null ;
        TableName name = TableName.valueOf(tableName);
        try{
            conn = HbasePool.getInstance().borrowObject();
            admin = conn.getAdmin();
            if(admin.tableExists(name)){
                //删除表之前先对其disable
                admin.disableTable(name);
                admin.deleteTable(name);
            }else{
                log.warn("[{}] is not exist",tableName);
            }
        }catch(Exception e){
            log.error(e.toString());
        }finally{
            Utils.close(admin);
            HbasePool.getInstance().returnObject(conn);
        }
    }


    /**
     * 插入行
     * @param tableName
     * @param rowKey
     * @param family
     * @param qualifier
     * @param value
     */
    public static void put(String tableName , String rowKey , String family , String qualifier , String value) throws Exception {
        put(tableName,rowKey,family,qualifier, Bytes.toBytes(value));
    }

    /**
     * 插入行
     * @param tableName     表名
     * @param rowKey        行键
     * @param family        列簇
     * @param qualifier     修饰符
     * @param value         值
     * @throws Exception
     */
    public static void put(String tableName , String rowKey , String family , String qualifier , byte[] value) throws Exception {
        Connection conn = null ;
        HTable table = null ;
        try {
            conn = HbasePool.getInstance().borrowObject();
            table = (HTable)conn.getTable(TableName.valueOf(tableName));
            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes(family),Bytes.toBytes(qualifier),value);
            table.put(put);
        }catch(Exception e){
            log.error(e.toString());
        }finally{
            Utils.close(table);
            HbasePool.getInstance().returnObject(conn);
        }
    }

    /**
     * 批量写入
     * @param tableName
     * @param puts
     */
    public static void puts(String tableName , List<Put> puts){
        HTable table = null ;
        Connection conn = null ;
        try{
            conn = HbasePool.getInstance().borrowObject();
            table = (HTable) conn.getTable(TableName.valueOf(tableName));
            table.put(puts);
        }catch(Exception e){

        }finally{

        }
    }

    /**
     * 删除行
     * @param tableName     表名
     * @param rowKey        列簇
     * @throws Exception
     */
    public static void delete(String tableName , String rowKey) throws Exception {
        delete(tableName,rowKey,null,null);
    }

    /**
     * 删除行
     * @param tableName     表名
     * @param rowKey        列簇
     * @param family        行键
     * @param quarifier     修饰符
     * @throws Exception
     */
    public static void delete(String tableName , String rowKey , String family , String quarifier) throws Exception {
        HTable table = null ;
        Connection conn = null ;
        try{
            conn = HbasePool.getInstance().borrowObject();
            table = (HTable) conn.getTable(TableName.valueOf(tableName));
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            if(!StringUtils.isEmpty(family)){
                delete.addFamily(Bytes.toBytes(family));
            }
            if(!StringUtils.isEmpty(quarifier)){
                delete.addColumn(Bytes.toBytes(family),Bytes.toBytes(quarifier));
            }

            table.delete(delete);
        }catch(Exception e){
            log.error(e.toString());
        }finally{
            Utils.close(table);
            HbasePool.getInstance().returnObject(conn);
        }
    }

}

