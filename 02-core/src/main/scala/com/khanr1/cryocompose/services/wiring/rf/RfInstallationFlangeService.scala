package com.khanr1.cryocompose.services.wiring.rf

import com.khanr1.cryocompose.wiring.rf.{ RfInstallationFlangeParam, RfInstallationFlange }
import com.khanr1.cryocompose.repositories.wiring.rf.RfInstallationFlangeRepository

/** Service trait for managing RF InstallationFlanges.
  *
  * @tparam F             the effect type, typically an instance of a monad like `Future` or `IO`.
  * @tparam RfConnectorID the type representing the RF connector identifier.
  * @tparam CategoryID    the type representing the category identifier.
  * @tparam TagID         the type representing the tag identifier.
  */
trait RfInstallationFlangeService[F[_], RfConnectorID, FlangeID, CategoryID, TagID]:

  /** Creates a new RF InstallationFlange connector.
    *
    * @param blk the parameters required to create the RF InstallationFlange.
    * @return an effectful computation resulting in the created RF InstallationFlange.
    */
  def create(blk: RfInstallationFlangeParam[RfConnectorID, CategoryID, TagID])
    : F[RfInstallationFlange[RfConnectorID, FlangeID, CategoryID, TagID]]

  /** Retrieves all RF InstallationFlange connectors.
    *
    * @return an effectful computation resulting in a vector of all RF InstallationFlange connectors.
    */
  def findAllRfInstallationFlange
    : F[Vector[RfInstallationFlange[RfConnectorID, FlangeID, CategoryID, TagID]]]

  /** Deletes an RF InstallationFlange connector based on its identifier.
    *
    * @param id the identifier of the RF InstallationFlange connector to delete.
    * @return an effectful computation resulting in `Unit` upon successful deletion.
    */
  def delete(id: RfConnectorID): F[Unit]

/** Companion object for the `RfInstallationFlangeService` trait.
  */
object RfInstallationFlangeService:

  /** Constructs an instance of `RfInstallationFlangeService` using the provided repository.
    *
    * @param repo the repository to interact with the data storage for RF InstallationFlanges.
    * @tparam F             the effect type, typically an instance of a monad like `Future` or `IO`.
    * @tparam RfConnectorID the type representing the RF connector identifier.
    * @tparam CategoryID    the type representing the category identifier.
    * @tparam TagID         the type representing the tag identifier.
    * @return an instance of `RfInstallationFlangeService` that uses the provided repository.
    */
  def make[F[_], RfConnectorID, FlangeID, CategoryID, TagID](
    repo: RfInstallationFlangeRepository[F, RfConnectorID, FlangeID, CategoryID, TagID]
  ): RfInstallationFlangeService[F, RfConnectorID, FlangeID, CategoryID, TagID] =
    new RfInstallationFlangeService[F, RfConnectorID, FlangeID, CategoryID, TagID]:
      override def create(connector: RfInstallationFlangeParam[RfConnectorID, CategoryID, TagID])
        : F[RfInstallationFlange[RfConnectorID, FlangeID, CategoryID, TagID]] =
        repo.create(connector)

      override def findAllRfInstallationFlange
        : F[Vector[RfInstallationFlange[RfConnectorID, FlangeID, CategoryID, TagID]]] =
        repo.findAll()

      override def delete(id: RfConnectorID): F[Unit] = repo.delete(id)
