package com.khanr1
package cryocompose
package repositories
package wiring
package rf

import com.khanr1.cryocompose.wiring.rf.{ RfSet, RfSetParam }

/** A trait representing a repository for managing RF sets.
  *
  * @tparam F the effect type, representing the context in which operations are performed
  * @tparam RfAssemblyID the type of the identifier for RF assemblies
  * @tparam RfConnectorID the type of the identifier for RF connectors
  * @tparam CategoryID the type of the identifier for categories
  * @tparam TagID the type of the identifier for tags
  */
trait RfSetRepository[F[_], RfAssemblyID, RfConnectorID, CategoryID, TagID]:

  /** Creates a new RF set based on the provided parameters.
    *
    * @param param the parameters for creating the new RF set
    * @return an effect containing the created RF set
    */
  def create(param: RfSetParam[RfAssemblyID, RfConnectorID, CategoryID, TagID])
    : F[RfSet[RfAssemblyID, RfConnectorID, CategoryID, TagID]]

  /** Deletes the RF set with the given identifier.
    *
    * @param id the identifier of the RF set to delete
    * @return an effect representing the completion of the delete operation
    */
  def delete(id: RfAssemblyID): F[Unit]

  /** Finds all RF sets in the repository.
    *
    * @return an effect containing a vector of all RF sets
    */
  def findAll(): F[Vector[RfSet[RfAssemblyID, RfConnectorID, CategoryID, TagID]]]
