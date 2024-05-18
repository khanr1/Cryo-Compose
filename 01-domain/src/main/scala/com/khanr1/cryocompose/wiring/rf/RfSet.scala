package com.khanr1
package cryocompose
package wiring
package rf

import cats.Show

/** A final case class representing a set of RF assemblies.
  *
  * @param id the unique identifier of the RF set
  * @param rfAssemblies the list of RF assemblies composing the set
  * @param categoryID the category identifier associated with the RF set
  * @param tagsID the set of tag identifiers associated with the RF set
  * @tparam RfAssemblyID the type of the identifier for RF assemblies
  * @tparam RfConnectorID the type of the identifier for RF connectors
  * @tparam CategoryID the type of the identifier for categories
  * @tparam TagID the type of the identifier for tags
  * @extends Product An extension of the [[Product]] trait
  */
final case class RfSet[RfAssemblyID, RfConnectorID, CategoryID, TagID](
  id: RfAssemblyID,
  rfAssemblies: List[RfAssembly[RfAssemblyID, RfConnectorID, CategoryID, TagID]],
  categoryID: CategoryID,
  tagsID: Set[TagID],
) extends Product[RfAssemblyID, CategoryID, TagID]:

  /** The code representing the connectors in the RF set. */
  val connectorCode = rfAssemblies.head.connectors.map(_.name).mkString("-")

  /** A string representation of the distinct connectors' names in the RF set. */
  val connector = rfAssemblies.head.connectors.distinct.map(_.name).mkString("-")

  /** The code representing the wire material of the first RF assembly in the set. */
  val lineCode = rfAssemblies.head.line.wire.material.toString

  /** The code representing the unique lengths of wires in the RF set. */
  val lengthCode =
    val list = rfAssemblies.map(_.line.wire.length)
    val occurrences = list.groupBy(identity).view.mapValues(_.size).toMap
    val uniqueElements = list.filter(element => occurrences(element) == 1)
    uniqueElements.mkString("-")

  val setElement = rfAssemblies
    .sortBy(_.line.wire.length)
    .map(rfAssembly => rfAssembly.productName)
    .mkString("\n")

  /** The product code for the RF set. */
  override val code: ProductCode = ProductCode.applyUnsafe(connectorCode + lineCode + lengthCode)

  /** The product description for the RF set. */
  override val productDescription: ProductDescription =
    ProductDescription.applyUnsafe(
      s"Semi-rigid coaxial line set $connectorCode $lineCode $lengthCode:\n $setElement"
    )

  /** The product name for the RF set. */
  override val productName: ProductName =
    ProductName.applyUnsafe(s"Semi-rigid coaxial line set $connector $lineCode $lengthCode")

  /** The unique identifier for the RF set. */
  override val productID: RfAssemblyID = id
