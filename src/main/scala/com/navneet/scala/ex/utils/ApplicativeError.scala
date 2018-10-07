package com.navneet.scala.ex.utils

import scalaz._, Scalaz._

trait ApplicativeError[F[_], E] {
  val applicative: Applicative[F]
  def raiseError[A](e: E): F[A]
}
