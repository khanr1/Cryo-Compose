package com.khanr1
package cryocompose
package services

/** The TagService trait provides a generic interface
  *  for performing CRUD (Create, Read, Delete) operations on tags.
  *  It is designed to be flexible and can be implemented with various storage mechanisms,
  *  such as in-memory collections, databases, or external services.
  */

trait TagService[F[_], TagID]:
  /* Create Operation */
  def createTag(name: TagName): F[Tag[TagID]]
  /* Read Operation */
  def findAllTag: F[List[Tag[TagID]]]
  /* Delete Operation*/
  def deleteTag(id: TagID): F[Unit]
