package com.navneet.scala.ex.service

import scalaz._
import monix.eval.Task

trait Console[F[_]] {
  def readLine: F[String]
  def printLn(line: String): F[Unit]
}

object ForTrans {
  // To lift a F[A] type class Which is for Console to a Monad Transformer T[F,A]
  def forTrans[F[_]: Monad, T[_[_], _]: MonadTrans](
    implicit C: Console[F]): Console[T[F, ?]] = new Console[T[F, ?]] {

    // MonadTrans[T].liftM  lift F[A] to Monad Transformer T[F,A] here A is String since we are reading a line
    def readLine: T[F, String] = MonadTrans[T].liftM(Console[F].readLine)

    def printLn(str: String): T[F, Unit] =
        MonadTrans[T].liftM(Console[F].printLn(str))
  }
}

trait Console0 {
  // Either Error Or A EitherT[F, A]
  // EitherT[F[_], A, B] is a lightweight wrapper for F[Either[A, B]] =>
  implicit def eitherTConsole[F[_]: Monad: Console, E]: Console[EitherT[F, E, ?]] =
    ForTrans.forTrans[F, EitherT[?[_], E, ?]]
}

object Console  extends Console0 {
  def apply[F[_]: Console]: Console[F] = implicitly[Console[F]]

  val monixConsole: Console[Task] = new Console[Task] {
    override def readLine: Task[String] = Task.delay(scala.io.StdIn.readLine)
    override def printLn(line: String): Task[Unit] = Task.delay(println(line))
  }
}
