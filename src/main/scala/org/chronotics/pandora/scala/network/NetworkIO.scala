package org.chronotics.pandora.scala.network

import java.net.ServerSocket

object NetworkIO {
  def assignNewPort(): Int = {
    var intNewPort: Int = -1
    try {
      val socket = new ServerSocket(0)
      intNewPort = socket.getLocalPort

      socket.close()
    } catch {
      case ex: Throwable =>
    }

    intNewPort
  }
}
