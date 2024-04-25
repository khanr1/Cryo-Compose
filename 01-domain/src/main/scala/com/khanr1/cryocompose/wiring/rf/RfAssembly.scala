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
  line: RfLine[RfConnectorID, CategoryID, TagID],
  tags: Set[TagID],
) extends WiringAssembly(connectors, List(line))
       with Product[RfAssemblyID, CategoryID, TagID]:
  override val code: ProductCode =
    val connectorsCode = connectors.map(_.code.show).mkString("(", "-", ")")
    val lineCode = line.toString()
    val full = connectorsCode + "-" + lineCode
    ProductCode.applyUnsafe(full)
  override val productDescription: ProductDescription = ???
  override val productID: RfAssemblyID = id
  override val productName: ProductName = ???
  override val categoryID: CategoryID = categoryID
  override val tagsID: Set[TagID] = tags
