package com.khanr1
package cryocompose
package services

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
  /** Create a category
    *
    * @param category
    * @return
    */
  def createCategory(category: CategoryParam[CategoryID]): F[CategoryID]
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
