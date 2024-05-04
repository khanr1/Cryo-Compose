package com.khanr1
package cryocompose
package components
package crudProductTable

import com.raquo.laminar.api.L.{ *, given }

def tableRow[ProductID, CategoryID, TagID](product: Product[ProductID, CategoryID, TagID]) =
  tr(
    td(product.productID.toString()),
    td(product.code.value.show),
    td(product.productName.value.show),
    td(product.categoryID.toString()),
    td(product.tagsID.mkString("[", ",", "]")),
    td(
      button(
        typ := "button",
        cls := "btn btn--outline-secondary",
        dataAttr("bs-toggle") := "modal",
        dataAttr("bs-target") := "#exampleModal",
        i(cls := "bi bi-info-square-fill"),
      )
    ),
  )
