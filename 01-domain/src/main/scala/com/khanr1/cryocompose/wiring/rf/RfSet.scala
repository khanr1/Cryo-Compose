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
  val connectorCode: String = rfAssemblies.head.connectors.map(_.name).mkString("-")

  /** A string representation of the distinct connectors' names in the RF set. */
  val connector: String = rfAssemblies.head.connectors.distinct.map(_.name).mkString("-")

  /** The code representing the wire material of the first RF assembly in the set. */
  val lineCode: String = rfAssemblies.head.line.wire.material.toString

  /** The code representing the unique lengths of wires in the RF set. */
  val lengthCode: String =
    val list = rfAssemblies.map(_.line.wire.length).sorted.flatMap(s => s.show.split("_"))
    val occurrences = list.groupBy(identity).view.mapValues(_.size).toMap
    val uniqueElements = list.filter(element => occurrences(element) == 1)
    uniqueElements.mkString("-")

  /** The string representation of the RF set elements, sorted by wire length. */
  val setElement: String = rfAssemblies
    .sortBy(_.line.wire.length)
    .map(rfAssembly => rfAssembly.productName.value)
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

object RfSet:
  /** Show instance for RfSet, using the default toString representation. */
  given show[RfAssemblyID, RfConnectorID, CategoryID, TagID]
    : Show[RfSet[RfAssemblyID, RfConnectorID, CategoryID, TagID]] =
    Show.fromToString

  /** Decoder instance for RfSet. */
  given decoder[RfAssemblyID: Decoder, RfConnectorID: Decoder, CategoryID: Decoder, TagID: Decoder]
    : Decoder[RfSet[RfAssemblyID, RfConnectorID, CategoryID, TagID]] =
    Decoder.forProduct4("id", "rfAssemblies", "category", "tags")(
      RfSet[RfAssemblyID, RfConnectorID, CategoryID, TagID](_, _, _, _)
    )

  /** Encoder instance for RfSet. */
  given encoder[RfAssemblyID: Encoder, RfConnectorID: Encoder, CategoryID: Encoder, TagID: Encoder]
    : Encoder[RfSet[RfAssemblyID, RfConnectorID, CategoryID, TagID]] =
    Encoder.forProduct4("id", "rfAssemblies", "category", "tags")(a =>
      (a.id, a.rfAssemblies, a.categoryID, a.tagsID)
    )

/** A final case class representing the parameters needed to create an RF set.
  *
  * @param rfAssemblies the list of RF assemblies composing the set
  * @param categoryID the category identifier associated with the RF set
  * @param tagsID the set of tag identifiers associated with the RF set
  * @tparam RfAssemblyID the type of the identifier for RF assemblies
  * @tparam RfConnectorID the type of the identifier for RF connectors
  * @tparam CategoryID the type of the identifier for categories
  * @tparam TagID the type of the identifier for tags
  */
final case class RfSetParam[RfAssemblyID, RfConnectorID, CategoryID, TagID](
  rfAssemblies: List[RfAssembly[RfAssemblyID, RfConnectorID, CategoryID, TagID]],
  categoryID: CategoryID,
  tagsID: Set[TagID],
)

object RfSetParam:
  /** Show instance for RfSetParam, using the default toString representation. */
  given show[RfAssemblyID, RfConnectorID, CategoryID, TagID]
    : Show[RfSetParam[RfAssemblyID, RfConnectorID, CategoryID, TagID]] =
    Show.fromToString

  /** Decoder instance for RfSetParam. */
  given decoder[RfAssemblyID: Decoder, RfConnectorID: Decoder, CategoryID: Decoder, TagID: Decoder]
    : Decoder[RfSetParam[RfAssemblyID, RfConnectorID, CategoryID, TagID]] =
    Decoder.forProduct3("rfAssemblies", "category", "tags")(
      RfSetParam[RfAssemblyID, RfConnectorID, CategoryID, TagID](_, _, _)
    )

  /** Encoder instance for RfSetParam. */
  given encoder[RfAssemblyID: Encoder, RfConnectorID: Encoder, CategoryID: Encoder, TagID: Encoder]
    : Encoder[RfSetParam[RfAssemblyID, RfConnectorID, CategoryID, TagID]] =
    Encoder.forProduct3("rfAssemblies", "category", "tags")(a =>
      (a.rfAssemblies, a.categoryID, a.tagsID)
    )
