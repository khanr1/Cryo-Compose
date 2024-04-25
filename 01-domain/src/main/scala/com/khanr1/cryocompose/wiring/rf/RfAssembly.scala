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
final case class RfAssembly[RfAssemblyID, RfConnectorID, CategoryID, TagID](
  id: RfAssemblyID,
  connectors: List[RfConnector[RfConnectorID, CategoryID, TagID]],
  lines: List[RfLine[RfConnectorID, CategoryID, TagID]],
) extends WiringAssembly(connectors, lines)
       with Product[RfAssemblyID, CategoryID, TagID]:
  override val code: ProductCode = ???
  override val productDescription: ProductDescription = ???
  override val productID: RfAssemblyID = ???
  override val productName: ProductName = ???
  override val categoryID: CategoryID = ???
  override val tagsID: Set[TagID] = ???
