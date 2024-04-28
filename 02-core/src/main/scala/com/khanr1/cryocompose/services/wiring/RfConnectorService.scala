package com.khanr1
package cryocompose
package services
package wiring

import com.khanr1.cryocompose.wiring.rf.RfConnectorParam
import com.khanr1.cryocompose.wiring.rf.RfConnector
import com.khanr1.cryocompose.repositories.wiring.RfConnectorReposit

/** A trait representing a service for managing RF connectors.
  *
  * @tparam F the effect type representing the context in which operations are executed (e.g., `IO`, `Future`).
  * @tparam RfConnectorID the type of the identifier for RF connectors.
  * @tparam CategoryID the type of the identifier for categories.
  * @tparam TagID the type of the identifier for tags.
  */
trait RfConnectorService[F[_], RfConnectorID, CategoryID, TagID]:

  /** Creates a new RF connector based on the provided parameters.
    *
    * @param connector the parameters used to create the RF connector.
    * @return an effectful computation resulting in the created RF connector.
    */
  def create(connector: RfConnectorParam[CategoryID, TagID])
    : F[RfConnector[RfConnectorID, CategoryID, TagID]]

  /** Retrieves all RF connectors.
    *
    * @return an effectful computation resulting in a list of all RF connectors.
    */
  def findAllRfConnector: F[Vector[RfConnector[RfConnectorID, CategoryID, TagID]]]

  /** Deletes an RF connector based on its identifier.
    *
    * @param id the identifier of the RF connector to delete.
    * @return an effectful computation resulting in unit upon successful deletion.
    */
  def delete(id: RfConnectorID): F[Unit]

object RfConnectorService:
  def make[F[_], RfConnectorID, CategoryID, TagID](
    repo: RfConnectorReposit[F, RfConnectorID, CategoryID, TagID]
  ): RfConnectorService[F, RfConnectorID, CategoryID, TagID] =
    new RfConnectorService[F, RfConnectorID, CategoryID, TagID]:
      override def create(connector: RfConnectorParam[CategoryID, TagID])
        : F[RfConnector[RfConnectorID, CategoryID, TagID]] = repo.create(connector)

      override def findAllRfConnector: F[Vector[RfConnector[RfConnectorID, CategoryID, TagID]]] =
        repo.findAll()

      override def delete(id: RfConnectorID): F[Unit] = repo.delete(id)
