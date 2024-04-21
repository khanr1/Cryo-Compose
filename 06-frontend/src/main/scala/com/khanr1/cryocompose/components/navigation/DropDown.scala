package com.khanr1
package cryocompose
package components
package navigation

import cats.Show
import com.khanr1.cryocompose.utils.{ HasHierarchy, Tree }
import com.raquo.laminar.api.L.*
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom

def dropDown[ID, Entry <: HasHierarchy[ID]: Show](
  trees: List[Tree[ID, Entry]]
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
            tree.entry.show,
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
          dropDown(tree.children),
        ),
  )
