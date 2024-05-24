package com.khanr1
package cryocompose

import cats.effect.IO
import cats.effect.unsafe.implicits.*
import com.raquo.laminar.api.L.{ *, given }
import com.khanr1.cryocompose.wiring.rf.{ RfAssembly, RfSet }
import io.circe.syntax.*
import io.circe.parser.decode
import org.http4s.*
import org.http4s.circe.*
import org.http4s.implicits.*
import com.raquo.airstream.core

given entityDecoder: EntityDecoder[IO, List[Category[Int]]] =
  jsonOf

given rfAssemblyDecoder: EntityDecoder[IO, List[RfAssembly[Int, Int, Int, Int]]] =
  jsonOf

given rfSetDecoder: EntityDecoder[IO, List[RfSet[Int, Int, Int, Int]]] =
  jsonOf

def fetchedRfAssembly: core.EventStream[List[RfAssembly[Int, Int, Int, Int]]] = FetchStream
  .get("http://localhost:8080/rf/rfassembly")
  .map(response => response.text)
  .map(data => decode[List[RfAssembly[Int, Int, Int, Int]]](data))
  .collect {
    case Right(rfAssemblies) => rfAssemblies
  }

def fetchedRfSet: core.EventStream[List[RfSet[Int, Int, Int, Int]]] = FetchStream
  .get("http://localhost:8080/rf/rfSet")
  .map(response => response.text)
  .map(data => decode[List[RfSet[Int, Int, Int, Int]]](data))
  .collect {
    case Right(rfSet) => rfSet
  }

def fetchedCategory: core.EventStream[List[Category[Int]]] = FetchStream
  .get("http://localhost:8080/categories")
  .map(response => response.text)
  .map(data => decode[List[Category[Int]]](data))
  .collect {
    case Right(categories) => categories
  }
