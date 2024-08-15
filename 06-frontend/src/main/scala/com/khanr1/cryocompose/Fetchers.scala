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
import com.khanr1.cryocompose.wiring.rf.RfInstallationFlange
import com.khanr1.cryocompose.ports.Ports
import com.khanr1.cryocompose.stages.Stages
import com.khanr1.cryocompose.wiring.rf.RfBulkhead
import com.khanr1.cryocompose.wiring.rf.RfInstallationSet

given entityDecoder: EntityDecoder[IO, List[Category[Int]]] =
  jsonOf

given rfAssemblyDecoder: EntityDecoder[IO, List[RfAssembly[Int, Int, Int, Int]]] =
  jsonOf

given rfSetDecoder: EntityDecoder[IO, List[RfSet[Int, Int, Int, Int]]] =
  jsonOf

given rfbulkheadDecoder: EntityDecoder[IO, List[RfBulkhead[Int, Int, Int]]] =
  jsonOf

given rfFlangeDecoder: EntityDecoder[IO, List[RfInstallationFlange[Int, Int, Int, Int]]] =
  jsonOf

given rfInstSetDecoder: EntityDecoder[IO, List[RfInstallationSet[Int, Int, Int, Int]]] = jsonOf

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

def fetchedRfBulkhead: core.EventStream[List[RfBulkhead[Int, Int, Int]]] = FetchStream
  .get("http://localhost:8080/rf/rfbulkhead")
  .map(response => response.text)
  .map(data => decode[List[RfBulkhead[Int, Int, Int]]](data))
  .collect {
    case Right(rfFlange) =>
      rfFlange
  }

def fetchedRfFlange: core.EventStream[List[RfInstallationFlange[Int, Int, Int, Int]]] = FetchStream
  .get("http://localhost:8080/rf/rfflange")
  .map(response => response.text)
  .map(data => decode[List[RfInstallationFlange[Int, Int, Int, Int]]](data))
  .collect {
    case Right(rfFlange) =>
      rfFlange
  }

def fetchedRfInstSet: core.EventStream[List[RfInstallationSet[Int, Int, Int, Int]]] = FetchStream
  .get("http://localhost:8080/rf/rfflangeset")
  .map(response => response.text)
  .map(data => decode[List[RfInstallationSet[Int, Int, Int, Int]]](data))
  .collect {
    case Right(rfFlange) =>
      rfFlange
  }
