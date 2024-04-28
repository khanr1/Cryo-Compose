package com.khanr1
package cryocompose
package repositories
package wiring

import com.khanr1.cryocompose.wiring.rf.RfConnectorParam
import com.khanr1.cryocompose.wiring.rf.RfConnector

trait RfConnectorReposit[F[_], RfConnectorID, CategoryID, TagID]:
  def create(name: RfConnectorParam[CategoryID, TagID])
    : F[RfConnector[RfConnectorID, CategoryID, TagID]]
  def delete(id: RfConnectorID): F[Unit]
  def findAll(): F[Vector[RfConnector[RfConnectorID, CategoryID, TagID]]]
