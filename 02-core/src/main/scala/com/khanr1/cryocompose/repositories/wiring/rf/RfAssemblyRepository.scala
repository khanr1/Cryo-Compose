package com.khanr1
package cryocompose
package repositories
package wiring
package rf

import com.khanr1.cryocompose.wiring.rf.RfAssembly
import com.khanr1.cryocompose.wiring.rf.RfAssemblyParam
/** A trait representing a repository for managing RF assemblies.
  *
  * @tparam F the effect type representing the context in which operations are executed (e.g., `IO`, `Future`).
  * @tparam RfAssemblyID the type of the identifier for RF assemblies.
  * @tparam RfConnectorID the type of the identifier for RF connectors.
  * @tparam CategoryID the type of the identifier for categories.
  * @tparam TagID the type of the identifier for tags.
  */
trait RfAssemblyRepository[F[_], RfAssemblyID, RfConnectorID, CategoryID, TagID]:

  /** Creates a new RF assembly based on the provided parameters.
    *
    * @param param the parameters used to create the RF assembly.
    * @return an effectful computation resulting in the created RF assembly.
    */
  def create(param: RfAssemblyParam[RfConnectorID, CategoryID, TagID])
    : F[RfAssembly[RfAssemblyID, RfConnectorID, CategoryID, TagID]]

  /** Deletes an RF assembly based on its identifier.
    *
    * @param id the identifier of the RF assembly to delete.
    * @return an effectful computation resulting in unit upon successful deletion.
    */
  def delete(id: RfAssemblyID): F[Unit]

  /** Retrieves all RF assemblies.
    *
    * @return an effectful computation resulting in a vector of all RF assemblies.
    */
  def findAll(): F[Vector[RfAssembly[RfAssemblyID, RfConnectorID, CategoryID, TagID]]]
