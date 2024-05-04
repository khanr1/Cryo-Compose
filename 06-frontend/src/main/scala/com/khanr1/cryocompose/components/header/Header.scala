package com.khanr1
package cryocompose
package components
package crudProductTable

import com.raquo.laminar.api.L.{ *, given }

def headerMain(title: String) =
  headerTag(
    cls := "bg-dark py-5",
    div(
      cls := "container px-4 px-lg-5 my-5",
      div(
        cls := "text-center text-white",
        h1(cls := "display-4 fw-bolder", title),
        p(cls := "lead fw-normal text-white-50 mb-0", "trying to look ok"),
      ),
    ),
  )
