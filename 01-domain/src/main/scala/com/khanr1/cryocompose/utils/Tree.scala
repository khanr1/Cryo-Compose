package com.khanr1
package cryocompose
package utils

import scala.annotation.tailrec

/** Tree
  *
  * @param entry
  *   the data structure that has a hierachy
  * @param children
  *   the childen tree of the entry
  */
final case class Tree[ID, Entry <: HasHierarchy[ID]](
  entry: Entry,
  children: List[Tree[ID, Entry]],
)

/** Factory method for Tree
  */
object Tree:

  /** construc a list of tree from list of entries with hierarchy
    *
    * @param entries
    *   entry with hierarchy
    * @return
    *   List[Tree[ID, Entry]]
    */
  def construct[ID, Entry <: HasHierarchy[ID]](
    entries: List[Entry]
  ): List[Tree[ID, Entry]] =
    val entriesByParent: Map[Option[ID], List[Entry]] = entries.groupBy(_.parent)
    val rootEntry: List[Entry] = entriesByParent.getOrElse(None, List.empty)
    val bottomToTop: List[EntryWithDepth[ID, Entry]] = sortEntryByDepth(rootEntry, entriesByParent)
    val maxDepth: Int = bottomToTop.headOption.map(_.depth).getOrElse(0)
    buildFromBottom(maxDepth, bottomToTop, entriesByParent, Map.empty)

  // data structure to assign depth level to all data entry. The roots
  // (entry without parent) depth is 0
  private case class EntryWithDepth[ID, Entry <: HasHierarchy[ID]](
    entry: Entry,
    depth: Int,
  )

  // sorting nodes by depth from deeper depth to top
  private def sortEntryByDepth[ID, Entry <: HasHierarchy[ID]](
    nodesInDepth: List[Entry],
    nodesByParent: Map[Option[ID], List[Entry]],
  ): List[EntryWithDepth[ID, Entry]] =
    @tailrec
    def loop[ID, Entry <: HasHierarchy[ID]](
      depth: Int,
      nodesInDepth: List[Entry],
      nodesByParent: Map[Option[ID], List[Entry]],
      acc: List[EntryWithDepth[ID, Entry]],
    ): List[EntryWithDepth[ID, Entry]] =
      val withDepth = nodesInDepth.map(n => EntryWithDepth(n, depth))
      val calculated = withDepth ++ acc
      val children =
        nodesInDepth.flatMap(n => nodesByParent.getOrElse(Some(n.id), List.empty))
      if children.isEmpty then calculated
      else loop(depth + 1, children, nodesByParent, calculated)

    loop(0, nodesInDepth, nodesByParent, List.empty)

  @tailrec
  private def buildFromBottom[ID, Entry <: HasHierarchy[ID]](
    depth: Int,
    remaining: Seq[EntryWithDepth[ID, Entry]],
    entriesByParent: Map[Option[ID], List[Entry]],
    processedEntryByID: Map[ID, Tree[ID, Entry]],
  ): List[Tree[ID, Entry]] =

    val (entryOnDepth, rest) = remaining.span(_.depth == depth)
    val newProcessTree: Map[ID, Tree[ID, Entry]] = entryOnDepth.map { e =>
      val entryID: ID = e.entry.id
      val children: List[Tree[ID, Entry]] = entriesByParent
        .getOrElse(Some(entryID), List.empty)
        .flatMap(e => processedEntryByID.get(e.id))

      entryID -> Tree(e.entry, children)
    }.toMap

    if depth > 0 then
      buildFromBottom(
        depth - 1,
        rest,
        entriesByParent,
        processedEntryByID ++ newProcessTree,
      )
    else newProcessTree.values.toList

/** trait to add tree behavior
  */
trait HasHierarchy[A]:
  val id: A
  val parent: Option[A]
