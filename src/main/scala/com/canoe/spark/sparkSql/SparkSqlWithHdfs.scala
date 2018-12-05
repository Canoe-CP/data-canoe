package com.canoe.spark.sparkSql


import com.canoe.spark.beans.HdfsBean
import com.canoe.spark.util.DataUtil
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{SQLContext, SaveMode}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}
import org.slf4j.LoggerFactory

/**
  * Created by IntelliJ IDEA.
  * program: data-canoe
  * author:   Canoe
  * create: 2018-12-05 10:02
  */
object SparkSqlWithHdfs {
  private val LOG = LoggerFactory.getLogger(SparkSqlWithHdfs.getClass)

  def main(args:Array[String]): Unit ={
    val conf = new SparkConf().setAppName(this.getClass.getSimpleName)
    if(args.length == 0 || args == null){
      conf.setMaster("local[*]")
    }
    val sparkContext = new SparkContext(conf)
    val sqlContext = new SQLContext(sparkContext)

    val hdfsRDD = getHdfsInfo(sparkContext,sqlContext)
    val hdfsTable = "hdfsTable"
    sqlContext.createDataFrame(hdfsRDD).registerTempTable(hdfsTable)
    val sql = s"""|select
                  |*
                  |from
                  |${hdfsTable}
                  |where
                  |1=1
               """.stripMargin

    sqlContext.sql(sql).write.mode(SaveMode.Append)
          .jdbc(
                  DataUtil.getMySqlDriverInfo().get(1).concat(DataUtil.getMySqlDriverInfo().get(4)),
                  DataUtil.getTargetTableInfo().get(0),
                  DataUtil.getMySqlProp(DataUtil.getMySqlDriverInfo())
               )

  }

  def getHdfsInfo(sparkContext : SparkContext , sqlContext : SQLContext): RDD[HdfsBean] ={
    val hdfsRDD = sparkContext.textFile("/hdfs/mysql-215/canoe/canoe.txt")
                    .mapPartitions(x => {
                      var list = List[(HdfsBean)]()
                      x.foreach(row => {
                        val line = row.split(";")
                        list.::=(HdfsBean(line(0),line(1),line(2).toInt,line(3).toInt))
                      })
                      list.iterator
                    }).persist(StorageLevel.DISK_ONLY)
    hdfsRDD
  }
}

