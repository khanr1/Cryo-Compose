package com.khanr1
package cryocompose

import org.scalacheck.Gen
import org.scalacheck.Arbitrary

//generate nonEmpty string
val nonEmptyStringGen: Gen[String] =
  Gen
    .chooseNum(21, 40)
    .flatMap(n => Gen.buildableOfN[String, Char](n, Gen.alphaChar))
// generate A from nonempy string if String=> A exist
def nesGen[A](f: String => A): Gen[A] =
  nonEmptyStringGen.map(f)

val productNameGen: Gen[ProductName] =
  nonEmptyStringGen.map(ProductName.applyUnsafe(_))
val productDescriptionGen: Gen[ProductDescription] =
  nonEmptyStringGen.map(ProductDescription.applyUnsafe(_))
val productCodeGen: Gen[ProductCode] =
  nonEmptyStringGen.map(ProductCode.applyUnsafe(_))

val categoryNameGen: Gen[CategoryName] =
  nonEmptyStringGen.map(CategoryName.applyUnsafe(_))
val categoryDescriptionGen: Gen[CategoryDescription] =
  nonEmptyStringGen.map(CategoryDescription.applyUnsafe(_))

val tagNameGen: Gen[TagName] =
  nonEmptyStringGen.map(TagName.applyUnsafe(_))

def tagGen[A: Arbitrary]: Gen[Tag[A]] = for
  id <- Arbitrary.arbitrary[A]
  name <- tagNameGen
yield Tag[A](id, name)

def tagParamGen[A: Arbitrary]: Gen[TagParam[A]] = tagNameGen.flatMap(TagParam[A](_))

def categoryGen[A: Arbitrary]: Gen[Category[A]] = for
  a <- Arbitrary.arbitrary[A]
  name <- categoryNameGen
  description <- categoryDescriptionGen
  b <- Arbitrary.arbitrary[A]
yield Category(a, name, description, Some(b))
