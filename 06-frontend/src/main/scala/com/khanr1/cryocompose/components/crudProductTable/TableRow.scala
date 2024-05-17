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
        dataAttr("bs-target") := s"#${product.productID.toString()}",
        i(cls := "bi bi-info-square-fill"),
      )
    ),
    // Modal
    div(
      cls := "modal fade",
      idAttr := product.productID.toString(),
      tabIndex := -1,
      aria.labelledBy(s"label${product.productID.toString()}"),
      aria.hidden := true,
      div(
        cls := "modal-dialog",
        div(
          cls := "modal-content",
          div(
            cls := "modal-header",
            h1(
              cls := "modal-title fs-5",
              idAttr := "staticBackdropLabel",
              product.productName.toString(),
            ),
            button(
              typ := "button",
              cls := "btn-close",
              dataAttr("bs-dismiss") := "modal",
              aria.label := "Close",
            ),
          ),
          div(
            cls := "modal-body",
            p(product.productDescription.value),
          ),
          div(
            cls := "modal-footer",
            button(
              tpe := "button",
              cls := "btn btn-secondary",
              dataAttr("bs-dismiss") := "modal",
              "Close",
            ),
          ),
        ),
      ),
    ),
  )
