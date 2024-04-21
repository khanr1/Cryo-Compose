package com.khanr1
package cryocompose
package components
package navigation

import cats.Show
import com.raquo.laminar.api.L.{ *, given }
import com.raquo.laminar.defs.attrs.AriaAttrs
import com.raquo.laminar.keys.AriaAttr
import utils.{ HasHierarchy, Tree }

def navigationBar[ID, Entry <: HasHierarchy[ID]: Show](
  trees: List[Tree[ID, Entry]]
) =
  div(
    cls := ("navbar", " navbar-expand-lg", " bg-body-tertiary"),
    div(
      cls := "container-fluid",
      a(cls := "navbar-brand", href := "#", "Navbar"),
      navBarTogglerButton("#navbarSupportedContent"),
      div(
        cls := ("collapse", "navbar-collapse"),
        idAttr := "navbarSupportedContent",
        menuItems(trees),
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
