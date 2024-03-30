package com.khanr1
package cryocompose
package suite

import cats.effect.IO
import cats.implicits.*
import io.circe.*
import io.circe.syntax.*
import org.http4s.*
import org.http4s.circe.*
import weaver.scalacheck.Checkers
import weaver.{ Expectations, SimpleIOSuite }

/** A trait representing a suite of HTTP test cases. It extends SimpleIOSuite and Checkers traits.
  * Provides utilities for testing HTTP routes.
  */
trait HttpSuite extends SimpleIOSuite with Checkers:

  /** Expects the response body and status code of an HTTP request to match the provided expectations.
    *
    * @param routes The HTTP routes to test.
    * @param req The HTTP request to send to the routes.
    * @param expectedBody The expected body of the HTTP response.
    * @param expectedStatus The expected status code of the HTTP response.
    * @tparam A The type of the expected body, which must be encodable to JSON.
    * @return An IO containing the result of the expectations.
    */
  def expectHttpBodyAndStatus[A: Encoder](
    routes: HttpRoutes[IO],
    req: Request[IO],
  )(
    expectedBody: A,
    expectedStatus: org.http4s.Status,
  ): IO[Expectations] =
    routes.run(req).value.flatMap {
      case Some(resp) =>
        resp.asJson.map { json =>
          // Expectations form a multiplicative Monoid but we can also use other combinators like `expect.all`
          expect.same(resp.status, expectedStatus) |+| expect
            .same(json.dropNullValues, expectedBody.asJson.dropNullValues)
        }
      case None => IO.pure(failure("route not found"))
    }
