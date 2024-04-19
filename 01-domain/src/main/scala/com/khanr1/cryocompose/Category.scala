package com.khanr1
package cryocompose

import utils.HasHierarchy

import cats.Eq
import cats.Show
import io.circe.Decoder
import io.circe.Encoder
import io.github.iltotore.iron.circe.given
import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

/** A Categorie allow to regroup product into families
  *
  * @param id unique identifier
  * @param name the name of the category
  * @param description a description of the categories
  * @param parent the parent of the category
  */
final case class Category[CategoryID](
  id: CategoryID,
  name: CategoryName,
  description: CategoryDescription,
  parent: Option[CategoryID] = None,
) extends HasHierarchy[CategoryID]

object Category:
  given show[CategoryID]: Show[Category[CategoryID]] = Show.fromToString
  given eq[CategoryID]: Eq[Category[CategoryID]] = Eq.fromUniversalEquals
  given encoder[CategoryID: Encoder]: Encoder[Category[CategoryID]] =
    Encoder.forProduct4("id", "name", "description", "parent")(c =>
      (c.id, c.name, c.description, c.parent)
    )

/** CategoryParam is used to create a category
  *
  * @param name
  *   name for the Category
  * @param description
  *   description ofthe category
  * @param parent
  *   parent if the category is a subcategory
  */
final case class CategoryParam[CategoryID](
  name: CategoryName,
  description: CategoryDescription,
  parent: Option[CategoryID] = None,
)

object CategoryParam:
  given show[CategoryID]: Show[Category[CategoryID]] = Show.fromToString
  given eq[CategoryID]: Eq[Category[CategoryID]] = Eq.fromUniversalEquals
  given decoder[CategoryID](
    using
    d: Decoder[Option[CategoryID]]
  ): Decoder[CategoryParam[CategoryID]] =
    Decoder.forProduct3("name", "description", "parent")(CategoryParam[CategoryID](_, _, _))

type CategoryNameR = DescribedAs[Not[Empty], "The name of a category cannot be an empty"]
opaque type CategoryName = String :| CategoryNameR
object CategoryName extends RefinedTypeOps[String, CategoryNameR, CategoryName]

type CategoryDescriptionR =
  DescribedAs[Not[Empty], "The Description of a category cannot be an empty"]
opaque type CategoryDescription = String :| CategoryDescriptionR
object CategoryDescription extends RefinedTypeOps[String, CategoryDescriptionR, CategoryDescription]
