package com.khanr1
package cryocompose
package controllers

import org.http4s.*

import com.khanr1.cryocompose.helpers.Parse
import cats.Applicative

trait Controller[F[_]]:
  def routes: HttpRoutes[F]
