package com.khanr1
package cryocompose

export squants.space.Length
export squants.space.Millimeters
export squants.time.Frequency
import squants.time.Gigahertz
import cats.Show
import java.lang.module.ModuleDescriptor.Exports

export io.circe.{ Encoder, Decoder }
export cats.syntax.all.*
export io.github.iltotore.iron.cats.given
export io.github.iltotore.iron.constraint.any.DescribedAs

given frequencyDecoder: Decoder[Frequency] = Decoder.decodeBigDecimal.map(Gigahertz.apply(_))
given frequencyEncoder: Encoder[Frequency] = Encoder.encodeBigDecimal.contramap(_.value)
given frequencyShow: Show[Frequency] = Show.fromToString

given Ordering[Length] with
  def compare(x: Length, y: Length): Int =
    // Convert lengths to a common unit for comparison (e.g., meters)
    val xMeters = x.toMeters
    val yMeters = y.toMeters
    // Compare the values
    xMeters compare yMeters
