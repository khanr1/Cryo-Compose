package com.khanr1
package cryocompose
package wiring
package rf

import com.khanr1.cryocompose.wiring.Bulkhead

final case class RfBulkhead[RfConnectorID, CategoryID, TagID](
  id: RfConnectorID,
  connector: RfConnector[RfConnectorID, CategoryID, TagID],
  length: Length,
  isHermetic: Hermeticity,
  category: CategoryID,
  tags: Set[TagID],
) extends Bulkhead
       with Product[RfConnectorID, CategoryID, TagID]:
  override val categoryID: CategoryID = category
  override val productID: RfConnectorID = id
  override val code: ProductCode = ProductCode.assume(bulkheadCode)
  override val productDescription: ProductDescription = ProductDescription.assume(
    s"${connector.connectorName}| ${connector.gender.show} ${isHermetic.show} bulkhead connector"
  )
  override val tagsID: Set[TagID] = tags
  override val productName: ProductName =
    ProductName.assume(s"${connector.gender.show} ${isHermetic.show} bulkhead connector")
