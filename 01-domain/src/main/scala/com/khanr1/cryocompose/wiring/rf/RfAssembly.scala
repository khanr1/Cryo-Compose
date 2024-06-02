package com.khanr1
package cryocompose
package wiring
package rf

import cats.Show
import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*
import com.khanr1.cryocompose.Category.decoder

/** Representation for an RF assembly.
  *
  * @param id the unique identifier of the assembly
  * @param connectors the RF connectors composing the assembly
  * @param line the line composing the assembly
  * @param category the category identifier associated with the assembly
  * @param tags the set of tag identifiers associated with the assembly
  * @tparam RfAssemblyID the type of the identifier for RF assemblies
  * @tparam RfConnectorID the type of the identifier for RF connectors
  * @tparam CategoryID the type of the identifier for categories
  * @tparam TagID the type of the identifier for tags
  * @extends WiringAssembly An extension of the [[WiringAssembly]] trait
  * @extends Product An extension of the [[Product]] trait
  */
final case class RfAssembly[RfAssemblyID, RfConnectorID, CategoryID, TagID](
  id: RfAssemblyID,
  connectors: List[RfConnector[RfConnectorID, CategoryID, TagID]],
  line: RfLine[RfConnectorID, CategoryID, TagID],
  category: CategoryID,
  tags: Set[TagID],
) extends WiringAssembly
       with Product[RfAssemblyID, CategoryID, TagID]:
  override val lines = List(line)
  override val code: ProductCode =
    ProductCode.applyUnsafe(line.lineCode)

  override val productDescription: ProductDescription =
    val text =
      s"Semi-rigid coaxial line ${connectors.map(_.connectorName).mkString("-")} ${line.wire.description}"
    ProductDescription.applyUnsafe(text)

  override val productID: RfAssemblyID = id

  override val productName: ProductName =
    val text = connectors.distinct.map(_.connectorName).mkString("", "-", " ") +
      line
        .wire
        .material
        .show + s" semi-rigid coaxial line ${line.wire.stageLength.get.show}"
    ProductName.applyUnsafe(text)

  override val categoryID: CategoryID = category

  override val tagsID: Set[TagID] = tags

object RfAssembly:
  /** Show instance for RfAssembly, using the default toString representation. */
  given show[RfAssemblyID, RfConnectorID, CategoryID, TagID]
    : Show[RfAssembly[RfAssemblyID, RfConnectorID, CategoryID, TagID]] = Show.fromToString

  /** Decoder instance for RfAssembly. */
  given decoder[RfAssemblyID: Decoder, RfConnectorID: Decoder, CategoryID: Decoder, TagID: Decoder]
    : Decoder[RfAssembly[RfAssemblyID, RfConnectorID, CategoryID, TagID]] =
    Decoder.forProduct5("id", "connectors", "line", "category", "tag")(
      RfAssembly[RfAssemblyID, RfConnectorID, CategoryID, TagID](_, _, _, _, _)
    )

  /** Encoder instance for RfAssembly. */
  given encoder[RfAssemblyID: Encoder, RfConnectorID: Encoder, CategoryID: Encoder, TagID: Encoder]
    : Encoder[RfAssembly[RfAssemblyID, RfConnectorID, CategoryID, TagID]] =
    Encoder.forProduct5("id", "connectors", "line", "category", "tag")(a =>
      (a.id, a.connectors, a.line, a.category, a.tags)
    )

/** Used to create an RF Assembly.
  *
  * @param connectors the RF connectors composing the assembly
  * @param line the line composing the assembly
  * @param category the category identifier associated with the assembly
  * @param tags the set of tag identifiers associated with the assembly
  * @tparam RfConnectorID the type of the identifier for RF connectors
  * @tparam CategoryID the type of the identifier for categories
  * @tparam TagID the type of the identifier for tags
  */
final case class RfAssemblyParam[RfConnectorID, CategoryID, TagID](
  connectors: List[RfConnector[RfConnectorID, CategoryID, TagID]],
  line: RfLine[RfConnectorID, CategoryID, TagID],
  category: CategoryID,
  tags: Set[TagID],
) extends WiringAssembly:
  override val lines = List(line)

object RfAssemblyParam:
  /** Show instance for RfAssemblyParam, using the default toString representation. */
  given show[RfConnectorID, CategoryID, TagID]
    : Show[RfAssemblyParam[RfConnectorID, CategoryID, TagID]] = Show.fromToString

  /** Decoder instance for RfAssemblyParam. */
  given decoder[RfConnectorID: Decoder, CategoryID: Decoder, TagID: Decoder]
    : Decoder[RfAssemblyParam[RfConnectorID, CategoryID, TagID]] =
    Decoder.forProduct4("connectors", "line", "category", "tag")(
      RfAssemblyParam[RfConnectorID, CategoryID, TagID](_, _, _, _)
    )

  /** Encoder instance for RfAssemblyParam. */
  given encoder[RfConnectorID: Encoder, CategoryID: Encoder, TagID: Encoder]
    : Encoder[RfAssemblyParam[RfConnectorID, CategoryID, TagID]] =
    Encoder.forProduct4("connectors", "line", "category", "tag")(a =>
      (a.connectors, a.line, a.category, a.tags)
    )
