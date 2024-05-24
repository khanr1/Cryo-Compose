package com.khanr1
package cryocompose
package components
package crudProductTable

import com.raquo.laminar.api.L.{ *, given }
import com.khanr1.cryocompose.wiring.rf.RfAssembly

def tableHeader[A, B, C] =
  thead(
    tr(
      for header <- Product.getAttributesName
      yield th(header),
      th("Details"),
    )
  )
