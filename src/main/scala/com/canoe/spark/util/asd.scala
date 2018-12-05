package com.hngs.untils

import java.io.File
import java.util.Properties
import java.util.logging.Logger

/**
  * program: data-canoe
  * author: canoe
  * create: 2018-10-22 15:49
  */
class PropertyUtil private(){
  private val property: Properties = new Properties();
  private val loader: ClassLoader = Thread.currentThread().getContextClassLoader();
  private var root: String = null;
  private val logger: Logger = Logger.getLogger("PropertyUtil");

  def this(resources: String) {
    this();
    property.load(loader.getResourceAsStream(resources));
  }

  def getHanlpProperty(key: String, defaultValue: String): String = {
    if (this.root == null) {
      root = property.getProperty("root");
      if (!root.endsWith("/")) {
        this.root += "/";
      }
      //判断此路径是否存在
      if (!fileUtil(this.root).exists()) {
        logger.warning("root=" + this.root + " 这个目录下没有data")
        return null;
      }
    }
    if ("root".equals(key)) {
      this.root;
    } else {
      var attriValue = property.getProperty(key);
      if (attriValue == null) {
        attriValue = defaultValue;
      }

      if (attriValue.startsWith("data") && ("CustomDictionaryPath" != key)) {
        var path = this.root + attriValue;
        if (fileUtil(path).exists() && fileUtil(path + ".bin").exists()) {
          path;
        } else {
          defaultValue;
        }
      } else {
        attriValue;
      }
    }
  }

  def getCommonPerperty(key: String, defaultValue: String): String = {
    property.getProperty(key, defaultValue);
  }

  def fileUtil(path: String): File = {
    new File(path);
  }
}

object PropertyUtil {
  @volatile private var instance: PropertyUtil = null ;

  /**
    * @param resources resources目录不需给路径， 直接传文件名， 配置文件名： *.properties
    * @return
    */
  def getInstance(resources: String): PropertyUtil = {
    if (instance == null) {
      synchronized {
        if (instance == null) {
          instance = new PropertyUtil(resources)
        }
      }
    }
    instance;
  }
}
