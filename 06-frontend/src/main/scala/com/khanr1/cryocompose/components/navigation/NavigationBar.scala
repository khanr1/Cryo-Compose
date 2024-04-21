package com.khanr1
package cryocompose
package components
package navigation

import com.raquo.laminar.api.L.{ *, given }
import com.raquo.laminar.defs.attrs.AriaAttrs
import com.raquo.laminar.keys.AriaAttr

def navbar =
  div(
    cls := ("navbar", " navbar-expand-lg", " bg-body-tertiary"),
    div(
      cls := "container-fluid",
      a(cls := "navbar-brand", href := "#", "Navbar"),
      button(
        cls := "navbar-toggler",
        tpe := "button",
        dataAttr("bs-toggle") := "collapse",
        dataAttr("bs-target") := "#navbarSupportedContent",
        aria.controls := "navbarSupportedContent",
        aria.expanded := false,
        aria.label := "Toggle navigation",
        span(cls := "navbar-toggler-icon"),
      ),
      div(
        cls := ("collapse", "navbar-collapse"),
        idAttr := "navbarSupportedContent",
        ul(
          cls := ("navbar-nav", "me-auto", "mb-2", "mb-lg-0"),
          li(
            cls := "nav-item",
            a(
              cls := ("nav-link", "active"),
              dataAttr("aria-current") := "page",
              href := "#",
              "Home",
            ),
          ),
          li(
            cls := "nav-item",
            a(cls := "nav-link", href := "#", "Link"),
          ),
          li(
            cls := ("nav-item", "dropdown"),
            a(
              cls := ("nav-link", "dropdown-toggle"),
              href := "#",
              role := "button",
              dataAttr("bs-toggle") := "dropdown",
              dataAttr("aria-expanded") := "false",
              "Dropdown",
            ),
            ul(
              cls := "dropdown-menu",
              li(
                a(cls := "dropdown-item", href := "#", "Action")
              ),
              li(
                a(cls := "dropdown-item", href := "#", "Another action")
              ),
              li(
                hr(cls := "dropdown-divider")
              ),
              li(
                a(cls := "dropdown-item", href := "#", "Something else here")
              ),
            ),
          ),
          li(
            cls := "nav-item",
            a(cls := ("nav-link", "disabled"), aria.disabled := true, "Disabled"),
          ),
        ),
        form(
          cls := "d-flex",
          role := "search",
          input(
            cls := ("form-control", "me-2"),
            tpe := "search",
            placeholder := "Search",
            aria.label := "Search",
          ),
          button(cls := ("btn", "btn-outline-success"), tpe := "submit", "Search"),
        ),
      ),
    ),
  )
