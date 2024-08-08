package com.khanr1
package cryocompose
package repositories
package inMemory

import cats.effect.kernel.Ref
import cats.MonadThrow
import com.khanr1.cryocompose.repositories.wiring.rf.RfBulkheadRepository
import com.khanr1.cryocompose.wiring.rf.RfBulkhead
import com.khanr1.cryocompose.wiring.rf.RfBulkheadParam

object RfBulkheadInMemoryRepository:
  def make[F[_]: MonadThrow](state: Ref[F, Vector[RfBulkhead[Int, Int, Int]]])
    : RfBulkheadRepository[F, Int, Int, Int] = new RfBulkheadRepository[F, Int, Int, Int]:
    private val nextInt: F[Int] = state
      .get
      .map(bulkheads => if bulkheads.isEmpty then 1 else bulkheads.map(_.id).max + 1)
    override def create(bulkhead: RfBulkheadParam[Int, Int, Int]): F[RfBulkhead[Int, Int, Int]] =
      nextInt
        .map(
          RfBulkhead(
            _,
            bulkhead.connector,
            bulkhead.length,
            bulkhead.isHermetic,
            bulkhead.category,
            bulkhead.tags,
          )
        )
        .flatMap(cat => state.modify(s => (s :+ cat) -> cat))

    override def delete(id: Int): F[Unit] = state.get.flatMap { connectors =>
      if connectors.exists(connector => connector.id === id) then
        state.update(x => x.filterNot(_.id == id))
      else
        MonadThrow[F]
          .raiseError(
            throw new java.lang.RuntimeException(
              s"Failed to delete connector with id ${id} because it didn't exist."
            )
          )

    }

    override def findAll(): F[Vector[RfBulkhead[Int, Int, Int]]] = state.get
