package com.khanr1
package cryocompose
package components
package navigation

import cats.Show
import com.khanr1.cryocompose.utils.{ HasHierarchy, Tree }
import com.raquo.laminar.api.L.*
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom

def dropDown[ID](
  trees: List[Tree[ID, Category[ID]]],
  linkID: Var[String],
): ReactiveHtmlElement[dom.html.UList] =
  ul(
    cls := "dropdown-menu",
    for tree <- trees
    yield
      if tree.children.isEmpty then
        li(
          a(
            cls := "dropdown-item",
            href := "#",
            dataAttr("description") := tree.entry.description.show,
            tree.entry.show,
            onClick.mapTo(tree.entry.name.value.show) --> linkID,
          )
        )
      else
        li(
          cls := "nav-item dropend",
          a(
            cls := "dropdown-item dropdown-toggle",
            href := "#",
            role := "button",
            dataAttr("bs-toggle") := "dropdown",
            dataAttr("bs-auto-close") := "outside",
            aria.expanded := false,
            tree.entry.show,
          ),
          dropDown(tree.children, linkID),
        ),
  )
