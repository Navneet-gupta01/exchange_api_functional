package com.navneet.scala.ex.utils

import scalaz.Scalaz._
import scalaz._

object MonadOps {
  def forever[F[_]: Monad, A](fa: F[A]): F[A] = fa >>= (v => forever(fa))
  implicit class RichMonad[F[_], A](val self: F[A])(implicit val F: Monad[F]) {
    def liftM[G[_[_], _]](implicit G: MonadTrans[G]): G[F, A] = G.liftM(self)
    def forever: F[A]                                         = MonadOps.forever(self)
  }
}
