package com.khanr1
package cryocompose
package repositories
package inMemory

import cats.MonadThrow
import cats.effect.kernel.Ref
import com.khanr1.cryocompose.wiring.given
import com.khanr1.cryocompose.wiring.rf.{ RfAssemblyParam, RfAssembly }
import com.khanr1.cryocompose.repositories.wiring.rf.RfAssemblyRepository

object RfAssemblyInMemoryRepository:
  def make[F[_]: MonadThrow](state: Ref[F, Vector[RfAssembly[Int, Int, Int, Int]]])
    : RfAssemblyRepository[F, Int, Int, Int, Int] = new RfAssemblyRepository[F, Int, Int, Int, Int]:
    private val nextInt: F[Int] = state
      .get
      .map(element => if element.isEmpty then 1 else element.map(_.categoryID).max + 1)

    override def create(element: RfAssemblyParam[Int, Int, Int])
      : F[RfAssembly[Int, Int, Int, Int]] =
      nextInt
        .map(RfAssembly(_, element.connectors, element.line, element.category, element.tags))
        .flatMap(cat => state.modify(s => (s :+ cat) -> cat))

    override def delete(id: Int): F[Unit] = state.get.flatMap { element =>
      if element.exists(element => element.id === id) then
        state.update(x => x.filterNot(_.id == id))
      else
        MonadThrow[F]
          .raiseError(
            throw new java.lang.RuntimeException(
              s"Failed to delete Rf Assembly  with id ${id} because it didn't exist."
            )
          )

    }

    override def findAll(): F[Vector[RfAssembly[Int, Int, Int, Int]]] =
      state.get.map(_.sortBy(_.line.wire.length))
