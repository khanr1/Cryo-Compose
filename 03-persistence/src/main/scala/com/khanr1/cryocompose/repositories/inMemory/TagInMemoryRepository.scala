package com.khanr1
package cryocompose
package repositories
package inMemory

import cats.effect.*
import cats.*

/** The TagInMemoryRepository object provides an implementation
  * of the TagRepository trait using an in-memory storage mechanism.
  * This implementation utilizes functional programming techniques
  *  with the Cats library for handling effects and state.
  */

object TagInMemoryRepository:
  def make[F[_]: Functor: FlatMap](state: Ref[F, Vector[Tag[Int]]]): TagRepository[F, Int] =
    type TagID = Int
    new TagRepository[F, TagID]:
      private val nextInt: F[Int] = state.get.map(_.size)

      override def create(name: TagName): F[Tag[TagID]] =
        nextInt
          .map(Tag(_, name))
          .flatMap(tag => state.modify(s => (s :+ tag) -> tag))

      override def delete(id: TagID): F[Unit] = state.update(x => x.filterNot(_.id === id))

      override def findAll(): F[Vector[Tag[TagID]]] = state.get
