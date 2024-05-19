package com.khanr1
package cryocompose
package repositories
package inMemory

import cats.MonadThrow
import com.khanr1.cryocompose.wiring.rf.{ RfSet, RfSetParam }
import cats.effect.kernel.Ref
import com.khanr1.cryocompose.repositories.wiring.rf.RfSetRepository

object RfSetInMemoryRepository:
  def make[F[_]: MonadThrow](state: Ref[F, Vector[RfSet[Int, Int, Int, Int]]])
    : RfSetRepository[F, Int, Int, Int, Int] = new RfSetRepository[F, Int, Int, Int, Int]:
    private val nextInt: F[Int] = state
      .get
      .map(element => if element.isEmpty then 1 else element.map(_.categoryID).max + 1)

    override def create(element: RfSetParam[Int, Int, Int, Int]): F[RfSet[Int, Int, Int, Int]] =
      nextInt
        .map(RfSet(_, element.rfAssemblies, element.categoryID, element.tagsID))
        .flatMap(cat => state.modify(s => (s :+ cat) -> cat))

    override def delete(id: Int): F[Unit] = state.get.flatMap { element =>
      if element.exists(element => element.id === id) then
        state.update(x => x.filterNot(_.id == id))
      else
        MonadThrow[F]
          .raiseError(
            throw new java.lang.RuntimeException(
              s"Failed to delete Rf Set  with id ${id} because it didn't exist."
            )
          )

    }

    override def findAll(): F[Vector[RfSet[Int, Int, Int, Int]]] =
      state.get
