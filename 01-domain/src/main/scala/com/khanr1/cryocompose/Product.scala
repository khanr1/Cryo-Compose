package com.khanr1
package cryocompose

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

/** A trait representing a product.
  *
  * @tparam ProductID the type of the identifier for products.
  * @tparam CategoryID the type of the identifier for categories.
  * @tparam TagID the type of the identifier for tags associated with products.
  */
trait Product[ProductID, CategoryID, TagID]:

  /** The identifier of the product. */
  val productID: ProductID
  /** The code of the product. */
  val code: ProductCode
  /** The name of the product. */
  val productName: ProductName
  /** The description of the product. */
  val productDescription: ProductDescription
  /** The identifier of the category to which the product belongs. */
  val categoryID: CategoryID
  /** The set of identifiers of tags associated with the product. */
  val tagsID: Set[TagID]

/** A refined type representing the name of a product. */
type ProductNameR =
  DescribedAs[Not[Empty], "The name of a product cannot be an empty"]
/** An opaque type representing the name of a product. */
opaque type ProductName = String :| ProductNameR

/** Utility functions and operations for working with `ProductName` opaque types */
object ProductName extends RefinedTypeOps[String, ProductNameR, ProductName]

/** A refined type representing the code of a product. */
type ProductCodeR =
  DescribedAs[Not[Empty], "The code of a product cannot be an empty"]

/** An opaque type representing the code of a product.
  */
opaque type ProductCode = String :| ProductCodeR

/** Utility functions and operations for working with `ProductCode` opaque types.
  */
object ProductCode extends RefinedTypeOps[String, ProductCodeR, ProductCode]

/** A refined type representing the description of a product.
  */
type DescriptionR =
  DescribedAs[Not[Empty], "The description of a product cannot be an empty"]

/** An opaque type representing the description of a product.
  */
opaque type ProductDescription = String :| DescriptionR

/** Utility functions and operations for working with `ProductDescription` opaque types.
  */
object ProductDescription extends RefinedTypeOps[String, DescriptionR, ProductDescription]
