package com.khanr1
package cryocompose
package services

import com.khanr1.cryocompose.repositories.CategoryRepository

trait CategoryService[F[_], CategoryID]:
  /** Finds the category by its name
    *
    * @param name
    * @return
    */
  def findByName(name: CategoryName): F[Option[Category[CategoryID]]]
  /** Finds a category from its id
    *
    * @param id
    * @return
    */
  def findByID(id: CategoryID): F[Option[Category[CategoryID]]]
  /** List all root, ie category that have no ancestor
    *
    * @return
    */
  def findRoot: F[Vector[Category[CategoryID]]]
  /** Find all the children of the category with identificator id
    *
    * @param id
    * @return
    */
  def findChildren(id: CategoryID): F[Vector[Category[CategoryID]]]
  /** find all the ancestor of the category with identificator id
    *
    * @param id
    * @return
    */
  def findAncetors(id: CategoryID): F[Vector[Category[CategoryID]]]

  def findAll: F[Vector[Category[CategoryID]]]
  /** Create a category
    *
    * @param category
    * @return
    */
  def createCategory(category: CategoryParam[CategoryID]): F[Category[CategoryID]]
  /** delete a category with id CategoryID
    *
    * @param id
    * @return
    */
  def deleteCatgory(id: CategoryID): F[Unit]
  /** update a category
    *
    * @param category
    * @return
    */
  def updateCategory(category: Category[CategoryID]): F[CategoryID]

object CategoryService:
  def make[F[_], CategoryID](repo: CategoryRepository[F, CategoryID])
    : CategoryService[F, CategoryID] =
    new CategoryService[F, CategoryID]:
      override def findChildren(id: CategoryID): F[Vector[Category[CategoryID]]] =
        repo.readChildren(id)

      override def findAncetors(id: CategoryID): F[Vector[Category[CategoryID]]] =
        repo.readAncestors(id)

      override def findByID(id: CategoryID): F[Option[Category[CategoryID]]] = repo.readByID(id)

      override def createCategory(category: CategoryParam[CategoryID]): F[Category[CategoryID]] =
        repo.create(category)

      override def findRoot: F[Vector[Category[CategoryID]]] = repo.readRoots

      override def findByName(name: CategoryName): F[Option[Category[CategoryID]]] =
        repo.readByName(name)

      override def updateCategory(category: Category[CategoryID]): F[CategoryID] =
        repo.update(category)

      override def deleteCatgory(id: CategoryID): F[Unit] = repo.delete(id)
      override def findAll: F[Vector[Category[CategoryID]]] = repo.readAll()
