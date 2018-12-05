package com.canoe.spark.util

import java.util
import java.util.Properties

import com.hngs.untils.PropertyUtil

/**
  * Created by IntelliJ IDEA.
  *
  * program: data-canoe
  * author:   Canoe
  * create: 2018-12-05 14:26
  */
object DataUtil {

  val prop = new PropertyUtil("data-canoe.properties")

  /**
    * 获取MySql驱动信息
    * @return
    */
  def getMySqlDriverInfo(): util.ArrayList[String] ={
    val list : util.ArrayList[String] = new util.ArrayList[String]()

    val mysql_driver = prop.getCommonPerperty("mysql_driver","")
    val mysql_url = prop.getCommonPerperty("mysql_url","")
    val mysql_user = prop.getCommonPerperty("mysql_user","")
    val mysql_password = prop.getCommonPerperty("mysql_password","")
    val hdfsDataBase = prop.getCommonPerperty("hdfsDataBase","")

    list.add(mysql_driver)
    list.add(mysql_url)
    list.add(mysql_user)
    list.add(mysql_password)
    list.add(hdfsDataBase)

    list
  }

  def getMySqlProp(list : util.ArrayList[String]): Properties={
    val prop = new Properties()

    prop.put("user",list.get(2))
    prop.put("password",list.get(3))
    prop.put("driver",list.get(0))
    prop.put("useSSL","false")

    prop
  }


  def getTargetTableInfo(): util.ArrayList[String] ={
    val list = new util.ArrayList[String]()

    list.add(prop.getCommonPerperty("hdfsTable",""))

    list
  }
}
