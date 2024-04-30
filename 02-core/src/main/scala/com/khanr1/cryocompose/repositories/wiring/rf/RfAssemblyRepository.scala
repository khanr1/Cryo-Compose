package com.khanr1
package cryocompose
package repositories
package wiring
package rf

import com.khanr1.cryocompose.wiring.rf.RfAssembly

trait RfAssemblyRepository[F[_], RfAssemblyID, RfConnectorID, CategoryID, TagID]:
  def create: F[RfAssembly[RfAssemblyID, RfConnectorID, CategoryID, TagID]]
