package com.khanr1.cryocompose.services.wiring.rf

import com.khanr1.cryocompose.wiring.rf.{ RfBulkheadParam, RfBulkhead }
import com.khanr1.cryocompose.repositories.wiring.rf.RfBulkheadRepository

/** Service trait for managing RF bulkheads.
  *
  * @tparam F             the effect type, typically an instance of a monad like `Future` or `IO`.
  * @tparam RfConnectorID the type representing the RF connector identifier.
  * @tparam CategoryID    the type representing the category identifier.
  * @tparam TagID         the type representing the tag identifier.
  */
trait RfBulkheadService[F[_], RfConnectorID, CategoryID, TagID]:

  /** Creates a new RF bulkhead connector.
    *
    * @param blk the parameters required to create the RF bulkhead.
    * @return an effectful computation resulting in the created RF bulkhead.
    */
  def create(blk: RfBulkheadParam[RfConnectorID, CategoryID, TagID])
    : F[RfBulkhead[RfConnectorID, CategoryID, TagID]]

  /** Retrieves all RF bulkhead connectors.
    *
    * @return an effectful computation resulting in a vector of all RF bulkhead connectors.
    */
  def findAllRfBulkhead: F[Vector[RfBulkhead[RfConnectorID, CategoryID, TagID]]]

  /** Deletes an RF bulkhead connector based on its identifier.
    *
    * @param id the identifier of the RF bulkhead connector to delete.
    * @return an effectful computation resulting in `Unit` upon successful deletion.
    */
  def delete(id: RfConnectorID): F[Unit]

/** Companion object for the `RfBulkheadService` trait.
  */
object RfBulkheadService:

  /** Constructs an instance of `RfBulkheadService` using the provided repository.
    *
    * @param repo the repository to interact with the data storage for RF bulkheads.
    * @tparam F             the effect type, typically an instance of a monad like `Future` or `IO`.
    * @tparam RfConnectorID the type representing the RF connector identifier.
    * @tparam CategoryID    the type representing the category identifier.
    * @tparam TagID         the type representing the tag identifier.
    * @return an instance of `RfBulkheadService` that uses the provided repository.
    */
  def make[F[_], RfConnectorID, CategoryID, TagID](
    repo: RfBulkheadRepository[F, RfConnectorID, CategoryID, TagID]
  ): RfBulkheadService[F, RfConnectorID, CategoryID, TagID] =
    new RfBulkheadService[F, RfConnectorID, CategoryID, TagID]:
      override def create(connector: RfBulkheadParam[RfConnectorID, CategoryID, TagID])
        : F[RfBulkhead[RfConnectorID, CategoryID, TagID]] = repo.create(connector)

      override def findAllRfBulkhead: F[Vector[RfBulkhead[RfConnectorID, CategoryID, TagID]]] =
        repo.findAll()

      override def delete(id: RfConnectorID): F[Unit] = repo.delete(id)
