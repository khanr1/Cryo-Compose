package com.khanr1
package cryocompose

import utils.HasHierarchy

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

type CategoryNameR = DescribedAs[Not[Empty], "The name of a category cannot be an empty"]
opaque type CategoryName = String :| CategoryNameR
object CategoryName extends RefinedTypeOps[String, CategoryNameR, CategoryName]

type CategoryDescriptionR =
  DescribedAs[Not[Empty], "The Description of a category cannot be an empty"]
opaque type CategoryDescription = String :| CategoryDescriptionR
object CategoryDescription extends RefinedTypeOps[String, CategoryDescriptionR, CategoryDescription]
