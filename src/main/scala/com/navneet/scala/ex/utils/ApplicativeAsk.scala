package com.navneet.scala.ex.utils

import scalaz.Applicative

trait ApplicativeAsk[F[_], E] {
  val applicative: Applicative[F]
  def ask: F[E]
  def reader[A](f: E => A): F[A]
}
