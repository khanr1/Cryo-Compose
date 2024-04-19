package com.khanr1
package cryocompose
package repositories
package inMemory

import cats.effect.Ref
import cats.*
import cats.implicits.*

/** The TagInMemoryRepository object provides an implementation
  * of the TagRepository trait using an in-memory storage mechanism.
  * This implementation utilizes functional programming techniques
  *  with the Cats library for handling effects and state.
  */

object TagInMemoryRepository:
  def make[F[_]: MonadThrow](state: Ref[F, Vector[Tag[Int]]]): TagRepository[F, Int] =
    type TagID = Int
    new TagRepository[F, TagID]:
      private val nextInt: F[Int] = state
        .get
        .map(tags =>
          if tags.isEmpty then 1
          else tags.map(t => t.id).max + 1
        )

      override def create(name: TagName): F[Tag[TagID]] =
        nextInt
          .map(Tag(_, name))
          .flatMap(tag => state.modify(s => (s :+ tag) -> tag))

      override def delete(id: TagID): F[Unit] =
        state.get.flatMap { tags =>
          if tags.exists(tag => tag.id === id) then state.update(x => x.filterNot(_.id === id))
          else
            MonadThrow[F]
              .raiseError(
                throw new java.lang.RuntimeException(
                  s"Failed to delete Tag with id ${id} because it didn't exist."
                )
              )

        }
        // state.update(x => x.filterNot(_.id === id))

      override def findAll(): F[Vector[Tag[TagID]]] = state.get
