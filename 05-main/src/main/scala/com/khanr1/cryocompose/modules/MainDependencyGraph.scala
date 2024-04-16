package com.khanr1
package cryocompose
package modules

import controllers.{ Controller, MainController }
import cats.Monad

object MainDependencyGraph:
  def make[F[_]: cats.effect.Sync]: F[Controller[F]] =
    MainController.make.pure
