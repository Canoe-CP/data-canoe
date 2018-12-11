package com.canoe.spark.hbase.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by IntelliJ IDEA.
 * program: data-canoe
 * author:   Canoe
 * create: 2018-12-05 15:26
 */
public class HbasePoolFactory extends BasePooledObjectFactory<Connection> {
    private static final Logger LOG = LoggerFactory.getLogger(HbasePoolFactory.class);


    @Override
    public Connection create() throws Exception {
        long start  = System.currentTimeMillis();
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        if(LOG.isDebugEnabled()){
            LOG.debug("Get hbase connection took {} ms", System.currentTimeMillis() - start);
        }
        return conn ;
    }

    @Override
    public PooledObject<Connection> wrap(Connection conn) {
        return new DefaultPooledObject<Connection>(conn);
    }


    @Override
    public void destroyObject(PooledObject<Connection> p) throws Exception {
        p.getObject().close();
        super.destroyObject(p);
    }
}
