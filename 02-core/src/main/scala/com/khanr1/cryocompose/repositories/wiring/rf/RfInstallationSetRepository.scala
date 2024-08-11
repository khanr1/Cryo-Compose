package com.khanr1
package cryocompose
package repositories
package wiring
package rf

import com.khanr1.cryocompose.wiring.rf.{ RfBulkhead, RfBulkheadParam }
import com.khanr1.cryocompose.wiring.rf.{ RfInstallationSet, RfInstallationSetParam }

trait RfInstallationSetRepository[F[_], RfConnectorID, FlangeID, CategoryID, TagID]:
  def create(blk: RfInstallationSetParam[RfConnectorID, FlangeID, CategoryID, TagID])
    : F[RfInstallationSet[RfConnectorID, FlangeID, CategoryID, TagID]]
  def delete(id: RfConnectorID): F[Unit]
  def findAll(): F[Vector[RfInstallationSet[RfConnectorID, FlangeID, CategoryID, TagID]]]
