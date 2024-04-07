package com.khanr1
package cryocompose
package helpers

import cats.syntax.all.*

/** Parse
  *
  * function from From to Either[Throwable, to] needs to be defined.
  */
trait Parse[-From, +To] extends Function1[From, Either[Throwable, To]]

object Parse:

  /** Implicit converter from String to Either[Throwable, Long]
    */
  given parseStringToLong: Parse[String, Long] =
    s =>
      Either
        .catchNonFatal(s.toLong)
        .leftMap { cause =>
          new IllegalArgumentException(
            s"""Attempt to conver $s to Long failed""",
            cause,
          )
        }
      /** Implicit converter from String to Either[Throwable, Int]
        */
  given parseStringToInt: Parse[String, Int] =
    s =>
      Either
        .catchNonFatal(s.toInt)
        .leftMap { cause =>
          new IllegalArgumentException(
            s"""Attempt to conver $s to Long failed""",
            cause,
          )
        }
