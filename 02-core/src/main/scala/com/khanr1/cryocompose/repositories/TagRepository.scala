package com.khanr1
package cryocompose
package repositories

/** The TagRepository trait defines a generic
  * interface for performing basic CRUD (Create, Read, Update, Delete)
  * operations on tags, using an effect type F[_] to represent various computational
  * effects. This trait is parameterized with TagID, which represents
  *  the unique identifier for tags.
  */
trait TagRepository[F[_], TagID]:
  def create(name: TagName): F[Tag[TagID]]
  def delete(id: TagID): F[Unit]
  def findAll(): F[Vector[Tag[TagID]]]
