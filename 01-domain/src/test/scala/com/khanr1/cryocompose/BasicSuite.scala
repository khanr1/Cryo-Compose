package com.khanr1
package cryocompose

import weaver.*
import weaver.scalacheck.Checkers
import weaver.scalacheck.CheckConfig

object BasicSuite extends SimpleIOSuite with Checkers:
  test("The ProductName construtor works properly") {
    forall(productNameGen) { x =>
      expect(x.value.length > 0)
    }
  }
  test("The ProductCode constructor works properly") {
    forall(productCodeGen) { x =>
      expect(x.value.length > 0)
    }
  }
  test("The ProductDescription constructor works properly") {
    forall(productDescriptionGen) { x =>
      expect(x.value.length > 0)
    }
  }

  test("The CategoryName constructor works properly") {
    forall(categoryNameGen) { x =>
      expect(x.value.length > 0)
    }
  }
  test("The CategoryDescription constructor works properly") {
    forall(categoryDescriptionGen) { x =>
      expect(x.value.length > 0)
    }
  }
