package com.khanr1
package cryocompose
package repositories
package wiring
package rf

import com.khanr1.cryocompose.wiring.rf.{ RfBulkhead, RfBulkheadParam }
import com.khanr1.cryocompose.wiring.rf.{ RfInstallationFlange, RfInstallationFlangeParam }

trait RfInstallationFlangeRepository[F[_], RfConnectorID, FlangeID, CategoryID, TagID]:
  def create(blk: RfInstallationFlangeParam[RfConnectorID, CategoryID, TagID])
    : F[RfInstallationFlange[RfConnectorID, FlangeID, CategoryID, TagID]]
  def delete(id: RfConnectorID): F[Unit]
  def findAll(): F[Vector[RfInstallationFlange[RfConnectorID, FlangeID, CategoryID, TagID]]]
