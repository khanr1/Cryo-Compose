package com.khanr1
package cryocompose
package wiring
package rf

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

import java.util.UUID

/** Representation for a RF assembly
  *
  * @param id
  *   the unique identifier of the assembly
  * @param connectors
  *   the RF connector composing the assembly
  * @param lines
  *   the line composing the assembly
  */
final case class RfAssembly[RfAssemblyID, RfConnectorID](
  id: RfAssemblyID,
  connectors: List[RfConnector[RfConnectorID]],
  lines: List[RfLine[RfConnectorID]],
) extends WiringAssembly(connectors, lines)
       with Product[RfAssemblyID]:
  override val productName = ???
  override val productDescription = ???
  override val code = ???
  override val productID = id
