package com.navneet.scala.ex.service

import scalaz._
import monix.eval.Task

trait Console[F[_]] {
  def readLine: F[String]
  def printLn(line: String): F[Unit]
}

object ForTrans {
  def forTrans[F[_]: Monad, T[_[_], _]: MonadTrans](
                                                     implicit C: Console[F]): Console[T[F, ?]] = new Console[T[F, ?]] {
    def readLine: T[F, String] = MonadTrans[T].liftM(Console[F].readLine)
    def printLn(str: String): T[F, Unit] =
      MonadTrans[T].liftM(Console[F].printLn(str))
  }
}

trait Console0 {
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
