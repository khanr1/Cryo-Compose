package com.khanr1
package cryocompose
package repositories
package inMemory

import cats.MonadThrow
import com.khanr1.cryocompose.wiring.rf.RfConnector
import cats.effect.kernel.Ref
import com.khanr1.cryocompose.repositories.wiring.rf.RfConnectorRepository
import com.khanr1.cryocompose.wiring.rf.RfConnectorParam

object RfConnectorInMemoryRepository:
  def make[F[_]: MonadThrow](state: Ref[F, Vector[RfConnector[Int, Int, Int]]])
    : RfConnectorRepository[F, Int, Int, Int] = new RfConnectorRepository[F, Int, Int, Int]:
    private val nextInt: F[Int] = state
      .get
      .map(connectors => if connectors.isEmpty then 1 else connectors.map(_.categoryID).max + 1)
    override def create(connector: RfConnectorParam[Int, Int]): F[RfConnector[Int, Int, Int]] =
      nextInt
        .map(RfConnector(_, connector.name, connector.gender, connector.category, connector.tags))
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

    override def findAll(): F[Vector[RfConnector[Int, Int, Int]]] = state.get
