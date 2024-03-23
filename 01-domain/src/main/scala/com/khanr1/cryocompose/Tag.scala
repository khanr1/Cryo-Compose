package com.khanr1
package cryocompose

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

/** Tag  can be added to a product for adding metadata information
  *
  * @param id
  *   is a unique identifier
  * @param name
  *   is the name of the Tag
  */
final case class Tag[TagID](id: TagID, name: TagName)

type TagNameR = DescribedAs[Not[Empty], "The name of a tag cannot be empty"]
opaque type TagName = String :| TagNameR
object TagName extends RefinedTypeOps[String, TagNameR, TagName]
