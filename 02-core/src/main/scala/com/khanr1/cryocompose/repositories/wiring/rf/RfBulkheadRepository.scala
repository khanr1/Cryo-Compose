package com.khanr1
package cryocompose
package repositories
package wiring
package rf

import com.khanr1.cryocompose.wiring.rf.{ RfBulkhead, RfBulkheadParam }

trait RfBulkheadRepository[F[_], RfConnectorID, CategoryID, TagID]:
  def create(blk: RfBulkheadParam[RfConnectorID, CategoryID, TagID])
    : F[RfBulkhead[RfConnectorID, CategoryID, TagID]]
  def delete(id: RfConnectorID): F[Unit]
  def findAll(): F[Vector[RfBulkhead[RfConnectorID, CategoryID, TagID]]]
