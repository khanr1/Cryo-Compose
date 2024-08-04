package com.khanr1
package cryocompose
package wiring
package rf

import com.khanr1.cryocompose.ports.Ports

final case class RfInstallationFlange[RfConnectorID, FlangeID, CategoryID, TagID](
  port: Ports,
  productID: FlangeID,
  categoryID: CategoryID,
  bulkheads: List[RfBulkhead[RfConnectorID, CategoryID, TagID]],
  tagsID: Set[TagID],
) extends InstallationFlange
       with Product[FlangeID, CategoryID, TagID]:
  override val numberSlot: NumberOfSlot = NumberOfSlot.applyUnsafe(bulkheads.length)
  override val size: Ports = port
  override val code: ProductCode =
    ProductCode.assume(
      s"${bulkheads.map(_.connector.connectorName).distinct.mkString("", "|", "")} $port RF-INST"
    )

  override val productName: ProductName = ProductName.assume(
    s"${bulkheads.map(_.connector.connectorName).distinct.mkString("", "|", "")} $port RF Installation Set "
  )

  override val productDescription: ProductDescription =
    val bulkheadsDistribution =
      bulkheads.groupBy(_.bulkheadCode).map((x, y) => (x, y.length)).mkString
    ProductDescription.applyUnsafe(
      s"RF installation set for $port with $bulkheadsDistribution"
    )
