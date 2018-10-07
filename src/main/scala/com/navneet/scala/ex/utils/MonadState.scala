package com.navneet.scala.ex.utils

import scalaz.Monad

trait MonadState[F[_], S] {
  val monad: Monad[F]
  def get: F[S]
  def set(s: S): F[Unit]
  def inspect[A](f: S => A): F[A]
  def modify(f: S => S): F[Unit]
}
