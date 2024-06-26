package com.khanr1
package cryocompose
package components
package navigation

import cats.Show
import cats.syntax.all.*
import com.raquo.laminar.api.L.{ *, given }
import utils.{ HasHierarchy, Tree }
import com.raquo.airstream.core.EventStream
import com.khanr1.cryocompose.wiring.rf.RfAssembly
import com.khanr1.cryocompose.wiring.rf.RfConnector

def menuItems[ProductID, RfConnectorID, CategoryID, TagID](
  trees: List[Tree[CategoryID, Category[CategoryID]]],
  linkID: Var[String],
) =
  ul(
    cls := "navbar-nav me-auto mb-2 mb-lg-0",
    for tree <- trees
    yield
      if tree.children.isEmpty then
        li(
          cls := "nav-item",
          a(
            cls := "nav-link",
            href := "#",
            tree.entry.name.value.show,
            onClick.mapTo(tree.entry.name.value.show) --> linkID,
          ),
        )
      else
        li(
          cls := "nav-item dropdown",
          a(
            cls := "nav-link dropdown-toggle",
            role := "button",
            dataAttr("bs-toggle") := "dropdown",
            dataAttr("bs-auto-close") := "outside",
            aria.expanded := false,
            href := "#",
            tree.entry.show,
          ),
          dropDown(tree.children, linkID),
        ),
  )
