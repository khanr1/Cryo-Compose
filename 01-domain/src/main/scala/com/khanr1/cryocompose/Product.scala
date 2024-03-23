package com.khanr1
package cryocompose

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

/** Product define the blue print of what a product needs to fulfill
  *
  * @param id
  *   the unique identifier
  * @param name
  *   the name of the product
  * @param code
  *   the product code
  */
trait Product[ProductID]:
  val productID: ProductID
  val code: ProductCode
  val productName: ProductName
  val productDescription: ProductDescription

type ProductNameR =
  DescribedAs[Not[Empty], "The name of a product cannot be an empty"]
opaque type ProductName = String :| ProductNameR
object ProductName extends RefinedTypeOps[String, ProductNameR, ProductName]

type ProductCodeR =
  DescribedAs[Not[Empty], "The code of a product cannot be an empty"]
opaque type ProductCode = String :| ProductCodeR
object ProductCode extends RefinedTypeOps[String, ProductCodeR, ProductCode]

type DescriptionR =
  DescribedAs[Not[Empty], "The description of a product cannot be an empty"]
opaque type ProductDescription = String :| DescriptionR
object ProductDescription extends RefinedTypeOps[String, DescriptionR, ProductDescription]
