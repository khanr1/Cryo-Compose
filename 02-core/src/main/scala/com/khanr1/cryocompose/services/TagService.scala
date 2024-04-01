package com.khanr1
package cryocompose
package services

import com.khanr1.cryocompose.repositories.TagRepository

/** The TagService trait provides a generic interface
  *  for performing CRUD (Create, Read, Delete) operations on tags.
  *  It is designed to be flexible and can be implemented with various storage mechanisms,
  *  such as in-memory collections, databases, or external services.
  */

trait TagService[F[_], TagID]:
  /* Create Operation */
  def createTag(name: TagName): F[Tag[TagID]]
  /* Read Operation */
  def findAllTag: F[Vector[Tag[TagID]]]
  /* Delete Operation*/
  def deleteTag(id: TagID): F[Unit]

object TagService:
  def make[F[_], TagID](repo: TagRepository[F, TagID]): TagService[F, TagID] =
    new TagService[F, TagID]:
      override def createTag(name: TagName): F[Tag[TagID]] = repo.create(name)
      override def deleteTag(id: TagID): F[Unit] = repo.delete(id)
      override def findAllTag: F[Vector[Tag[TagID]]] = repo.findAll()
