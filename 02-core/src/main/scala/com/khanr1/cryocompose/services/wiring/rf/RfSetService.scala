package com.khanr1
package cryocompose
package services
package wiring
package rf

import com.khanr1.cryocompose.wiring.rf.{ RfSetParam, RfSet }
import com.khanr1.cryocompose.repositories.wiring.rf.RfSetRepository

/** A trait representing a service for managing RF sets.
  *
  * @tparam F the effect type, representing the context in which operations are performed
  * @tparam RfAssemblyID the type of the identifier for RF assemblies
  * @tparam RfConnectorID the type of the identifier for RF connectors
  * @tparam CategoryID the type of the identifier for categories
  * @tparam TagID the type of the identifier for tags
  */
trait RfSetService[F[_], RfAssemblyID, RfConnectorID, CategoryID, TagID]:

  /** Creates a new RF set based on the provided parameters.
    *
    * @param param the parameters for creating the new RF set
    * @return an effect containing the created RF set
    */
  def create(param: RfSetParam[RfAssemblyID, RfConnectorID, CategoryID, TagID])
    : F[RfSet[RfAssemblyID, RfConnectorID, CategoryID, TagID]]

  /** Retrieves all RF sets.
    *
    * @return an effect containing a vector of all RF sets
    */
  def findAllRfSet: F[Vector[RfSet[RfAssemblyID, RfConnectorID, CategoryID, TagID]]]

  /** Deletes the RF set with the given identifier.
    *
    * @param id the identifier of the RF set to delete
    * @return an effect representing the completion of the delete operation
    */
  def delete(id: RfAssemblyID): F[Unit]

/** Companion object for the RfSetService trait. */
object RfSetService:

  /** Creates an instance of RfSetService.
    *
    * @param repo the repository to be used by the service
    * @return an instance of RfSetService
    */
  def make[F[_], RfAssemblyID, RfConnectorID, CategoryID, TagID](
    repo: RfSetRepository[F, RfAssemblyID, RfConnectorID, CategoryID, TagID]
  ): RfSetService[F, RfAssemblyID, RfConnectorID, CategoryID, TagID] =
    new RfSetService[F, RfAssemblyID, RfConnectorID, CategoryID, TagID]:

      /** Delegates the creation of an RF set to the repository.
        *
        * @param param the parameters for creating the new RF set
        * @return an effect containing the created RF set
        */
      override def create(param: RfSetParam[RfAssemblyID, RfConnectorID, CategoryID, TagID])
        : F[RfSet[RfAssemblyID, RfConnectorID, CategoryID, TagID]] = repo.create(param)

      /** Delegates the retrieval of all RF sets to the repository.
        *
        * @return an effect containing a vector of all RF sets
        */
      override def findAllRfSet: F[Vector[RfSet[RfAssemblyID, RfConnectorID, CategoryID, TagID]]] =
        repo.findAll()

      /** Delegates the deletion of an RF set to the repository.
        *
        * @param id the identifier of the RF set to delete
        * @return an effect representing the completion of the delete operation
        */
      override def delete(id: RfAssemblyID): F[Unit] = repo.delete(id)
