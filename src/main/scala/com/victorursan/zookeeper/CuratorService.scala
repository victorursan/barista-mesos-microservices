package com.victorursan.zookeeper

import org.apache.curator.framework.{CuratorFramework, CuratorFrameworkFactory}
import org.apache.curator.retry.ExponentialBackoffRetry

/**
  * Created by victor on 4/23/17.
  */
object CuratorService {
  private val zkConnection: String = "127.0.0.1:2181"
  private val client: CuratorFramework = CuratorFrameworkFactory.newClient(zkConnection, new ExponentialBackoffRetry(1000, 3))
  client.start()
  client.blockUntilConnected()

  def create(path: String, payload: Array[Byte], compressed: Boolean = false): Unit = if (compressed) {
    client.create()
      .creatingParentsIfNeeded()
      .forPath(path, payload)
  } else {
    client.create()
      .forPath(path, payload)
  }

  def update(path: String, payload: Array[Byte]): Unit =
    client.setData()
      .forPath(path, payload)

  def delete(path: String): Unit =
    client.delete()
      .deletingChildrenIfNeeded()
      .forPath(path)

  def read(path: String, decompressed: Boolean = false): Array[Byte] = if (decompressed) {
    client.getData
      .decompressed()
      .forPath(path)
  } else {
    client.getData
      .forPath(path)
  }
}
