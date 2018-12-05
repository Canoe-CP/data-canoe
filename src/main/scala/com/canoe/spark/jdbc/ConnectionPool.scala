package com.canoe.spark.jdbc

import java.sql.{Connection, DriverManager}
import java.util



/**
  * Created by IntelliJ IDEA.
  *
  * program: data-canoe
  * author:   Canoe
  * create: 2018-12-04 18:03
  * 简易mysql数据库连接池
  */
object ConnectionPool {
  private var connectNums = 0;                                   //当前连接池已产生的连接数
  private val connections = new util.LinkedList[Connection]()    //连接池


  try{
    Class.forName("com.mysql.jdbc.Driver")
  } catch{
    case e:  ClassCastException => println(e.toString)
  }

  /**
    * 获得连接
    */
  private def initConn() : Connection = {
    val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "123456")
    conn
  }

  /**
    * 初始化连接池
    */
  private def initConnectionPool() : util.LinkedList[Connection] = {
    AnyRef.synchronized({
      if(connections.isEmpty){
        for(i <- 1 until 10){
          connections.push(initConn())
          connectNums += 1
          println(i)
        }
      }
      connections
    })
  }

  /**
    * 获得连接
    */
  def getConn() :  Connection ={
    initConnectionPool()
    connections.poll()
  }

  /**
    * 释放连接
    */
  def releaseConn(con : Connection): Unit ={
    connections.push(con)
  }
}
