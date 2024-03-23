package com.khanr1
package cryocompose

import cats.effect.IO
import weaver._

object ExampleSuite extends SimpleIOSuite {
  test("make sure IO computes the right result..") {
    IO.pure(1).map(_ + 2) map { result =>
      expect.eql(result, 3)
    }
  }
}
