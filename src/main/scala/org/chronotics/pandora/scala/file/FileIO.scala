package org.chronotics.pandora.scala.file

import java.io._
import java.nio.file._

import org.chronotics.pandora.java.log.LoggerFactory

import scala.io.Source
import scala.compat.Platform.EOL

object FileIO {
  var _instance: FileIO = null

  var lockObject = new Object()

  def getInstance() = {
    lockObject.synchronized {
      if (_instance == null) {
        _instance = new FileIO()
      }
    }

    _instance
  }
}

class FileIO {
  val logger = LoggerFactory.getLogger(getClass)

  def readFile(strFilePath: String, isEntire: Boolean = false): List[String] = {
    try {
      var arrResult: List[String] = List.empty
      var strStr: StringBuilder = StringBuilder.newBuilder

      val objBuffered = Source.fromFile(strFilePath)

      for (line <- objBuffered.getLines()) {
        if (isEntire) {
          strStr = strStr.append(line)
          strStr = strStr.append(EOL)
        } else {
          arrResult = arrResult :+ line
        }
      }

      objBuffered.close()

      if (isEntire) {
        arrResult = arrResult :+ strStr.toString()
      }

      arrResult
    } catch {
      case ex: Throwable => {
        logger.error("ERR", ex)
        List.empty
      }
    }
  }

  def readBytesFromFile(strFilePath: String): Array[Byte] = {
    Files.readAllBytes(new File(strFilePath).toPath())
  }

  def writeFile(txt: String, strFilePath: String): Boolean = {
    try {
      var strDirPath = strFilePath.substring(0, strFilePath.lastIndexOf("/"))
      var objDir = new File(strDirPath)

      if (!objDir.exists()) {
        objDir.mkdirs()
      }

      val objFile = new File(strFilePath)
      val objBufferedWriter = new BufferedWriter(new FileWriter(objFile))

      objBufferedWriter.write(txt)
      objBufferedWriter.close()

      true
    } catch {
      case ex: Throwable => {
        logger.error("ERR", ex)
        false
      }
    }
  }

  def writeBytesToFile(arrBytes: Array[Byte], strFilePath: String): Boolean = {
    try {
      var strDirPath = strFilePath.substring(0, strFilePath.lastIndexOf("/"))
      var objDir = new File(strDirPath)

      if (!objDir.exists()) {
        objDir.mkdirs()
      }

      val bufferOutputStream = new BufferedOutputStream(new FileOutputStream(strFilePath))
      bufferOutputStream.write(arrBytes)
      bufferOutputStream.close()

      true
    } catch {
      case ex: Throwable => {
        logger.error("ERR", ex)
        false
      }
    }
  }
}
