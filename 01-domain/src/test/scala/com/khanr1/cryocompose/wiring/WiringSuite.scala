package com.khanr1
package cryocompose
package wiring

import weaver.*
import weaver.scalacheck.Checkers
import weaver.scalacheck.CheckConfig
import com.khanr1.cryocompose.wiring.rf.*

object WiringSuite extends SimpleIOSuite with Checkers:
  test("The RfConnector construtor works properly") {
    forall(rfConnectorGen[Int, Int, Int]) { c =>
      expect(c.connectorName.value.length() > 0 && c.numberPin.value > 0)
    }
  }

  test("The RfBulkhead construtor works properly") {
    forall(RfBulkheadGen[Int, Int, Int]) { c =>
      expect(c.connector.connectorName.value.length() > 0 && c.connector.numberPin.value > 0)
    }
  }
