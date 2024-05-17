package com.khanr1
package cryocompose
package components
package navigation

import cats.Show
import com.raquo.laminar.api.L.{ *, given }
import com.raquo.laminar.defs.attrs.AriaAttrs
import com.raquo.laminar.keys.AriaAttr
import utils.{ HasHierarchy, Tree }
import org.scalajs.dom.HTMLDivElement
import com.raquo.laminar.nodes.ReactiveHtmlElement

def navigationBar[ID](
  trees: List[Tree[ID, Category[ID]]], // here replace Category[ID] with enty to make tree generic
  linkID: Var[String],
): ReactiveHtmlElement[HTMLDivElement] =
  div(
    cls := ("navbar", " navbar-expand-lg", " bg-body-tertiary"),
    div(
      cls := "container-fluid",
      a(cls := "navbar-brand", href := "#", "Navbar"),
      navBarTogglerButton("#navbarSupportedContent"),
      div(
        cls := ("collapse", "navbar-collapse"),
        idAttr := "navbarSupportedContent",
        menuItems(trees, linkID),
      ),
    ),
  )

def navBarTogglerButton(target: String) = button(
  cls := "navbar-toggler",
  tpe := "button",
  dataAttr("bs-toggle") := "collapse",
  dataAttr("bs-target") := target,
  aria.controls := "navbarSupportedContent",
  aria.expanded := false,
  aria.label := "Toggle navigation",
  span(cls := "navbar-toggler-icon"),
)
