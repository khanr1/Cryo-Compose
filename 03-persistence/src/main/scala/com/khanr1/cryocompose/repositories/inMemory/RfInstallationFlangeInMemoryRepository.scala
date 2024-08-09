package com.khanr1
package cryocompose
package repositories
package inMemory

import cats.effect.kernel.Ref
import cats.MonadThrow
import com.khanr1.cryocompose.repositories.wiring.rf.RfInstallationFlangeRepository
import com.khanr1.cryocompose.wiring.rf.RfInstallationFlange
import com.khanr1.cryocompose.wiring.rf.RfInstallationFlangeParam

object RfInstallationFlangeInMemoryRepository:
  def make[F[_]: MonadThrow](state: Ref[F, Vector[RfInstallationFlange[Int, Int, Int, Int]]])
    : RfInstallationFlangeRepository[F, Int, Int, Int, Int] =
    new RfInstallationFlangeRepository[F, Int, Int, Int, Int]:
      private val nextInt: F[Int] = state
        .get
        .map(InstallationFlanges =>
          if InstallationFlanges.isEmpty then 1 else InstallationFlanges.map(_.productID).max + 1
        )
      override def create(InstallationFlange: RfInstallationFlangeParam[Int, Int, Int])
        : F[RfInstallationFlange[Int, Int, Int, Int]] =
        nextInt
          .map(
            RfInstallationFlange(
              _,
              InstallationFlange.port,
              InstallationFlange.bulkheads,
              InstallationFlange.stage,
              InstallationFlange.categoryID,
              InstallationFlange.tagsID,
            )
          )
          .flatMap(cat => state.modify(s => (s :+ cat) -> cat))

      override def delete(id: Int): F[Unit] = state.get.flatMap { connectors =>
        if connectors.exists(connector => connector.productID === id) then
          state.update(x => x.filterNot(_.productID == id))
        else
          MonadThrow[F]
            .raiseError(
              throw new java.lang.RuntimeException(
                s"Failed to delete connector with id ${id} because it didn't exist."
              )
            )

      }

      override def findAll(): F[Vector[RfInstallationFlange[Int, Int, Int, Int]]] = state.get
