package com.khanr1
package cryocompose
package wiring
package rf

import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import scala.collection.immutable
import squants.time.Gigahertz

def rfConnectorGen[A: Arbitrary, B: Arbitrary, C: Arbitrary]: Gen[RfConnector[A, B, C]] =
  for
    id <- Arbitrary.arbitrary[A]
    name <- connectorNameGen
    gender <- genderGen
    f <- Gen.posNum[Int]
    categoryID <- Arbitrary.arbitrary[B]
    tags <- Gen.listOf(Arbitrary.arbitrary[C])
  yield RfConnector[A, B, C](id, name, gender, Gigahertz(f), categoryID, Set.from(tags))
