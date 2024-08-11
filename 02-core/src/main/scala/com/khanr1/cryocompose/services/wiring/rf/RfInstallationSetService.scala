package com.khanr1.cryocompose.services.wiring.rf

import com.khanr1.cryocompose.wiring.rf.{ RfInstallationSetParam, RfInstallationSet }
import com.khanr1.cryocompose.repositories.wiring.rf.RfInstallationSetRepository

/** Service trait for managing RF InstallationFlanges.
  *
  * @tparam F             the effect type, typically an instance of a monad like `Future` or `IO`.
  * @tparam RfConnectorID the type representing the RF connector identifier.
  * @tparam CategoryID    the type representing the category identifier.
  * @tparam TagID         the type representing the tag identifier.
  */
trait RfInstallationSetService[F[_], RfConnectorID, FlangeID, CategoryID, TagID]:

  /** Creates a new RF InstallationSet connector.
    *
    * @param blk the parameters required to create the RF InstallationSet.
    * @return an effectful computation resulting in the created RF InstallationSet.
    */
  def create(blk: RfInstallationSetParam[RfConnectorID, FlangeID, CategoryID, TagID])
    : F[RfInstallationSet[RfConnectorID, FlangeID, CategoryID, TagID]]

  /** Retrieves all RF InstallationSet connectors.
    *
    * @return an effectful computation resulting in a vector of all RF InstallationSet connectors.
    */
  def findAllRfInstallationSet
    : F[Vector[RfInstallationSet[RfConnectorID, FlangeID, CategoryID, TagID]]]

  /** Deletes an RF InstallationSet connector based on its identifier.
    *
    * @param id the identifier of the RF InstallationSet connector to delete.
    * @return an effectful computation resulting in `Unit` upon successful deletion.
    */
  def delete(id: RfConnectorID): F[Unit]

/** Companion object for the `RfInstallationSetService` trait.
  */
object RfInstallationSetService:

  /** Constructs an instance of `RfInstallationSetService` using the provided repository.
    *
    * @param repo the repository to interact with the data storage for RF InstallationSets.
    * @tparam F             the effect type, typically an instance of a monad like `Future` or `IO`.
    * @tparam RfConnectorID the type representing the RF connector identifier.
    * @tparam CategoryID    the type representing the category identifier.
    * @tparam TagID         the type representing the tag identifier.
    * @return an instance of `RfInstallationSetService` that uses the provided repository.
    */
  def make[F[_], RfConnectorID, FlangeID, CategoryID, TagID](
    repo: RfInstallationSetRepository[F, RfConnectorID, FlangeID, CategoryID, TagID]
  ): RfInstallationSetService[F, RfConnectorID, FlangeID, CategoryID, TagID] =
    new RfInstallationSetService[F, RfConnectorID, FlangeID, CategoryID, TagID]:
      override def create(
        connector: RfInstallationSetParam[RfConnectorID, FlangeID, CategoryID, TagID]
      ): F[RfInstallationSet[RfConnectorID, FlangeID, CategoryID, TagID]] =
        repo.create(connector)

      override def findAllRfInstallationSet
        : F[Vector[RfInstallationSet[RfConnectorID, FlangeID, CategoryID, TagID]]] =
        repo.findAll()

      override def delete(id: RfConnectorID): F[Unit] = repo.delete(id)
