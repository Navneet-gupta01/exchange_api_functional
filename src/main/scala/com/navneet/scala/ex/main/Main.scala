package com.navneet.scala.ex.main

import com.navneet.scala.ex.models.Config
import com.navneet.scala.ex.utils.CTypes._
import scalaz._
import Scalaz._
import monix.eval.Task
import monix.execution.Scheduler
import shims._
import com.navneet.scala.ex.external.AppError
import com.navneet.scala.ex.service.Exchange
import com.navneet.scala.ex.service.Console
import com.navneet.scala.ex.utils.AtomicMonadState
import monix.execution.schedulers.SchedulerService
import scalaz.mtl.ApplicativeAsk
import com.navneet.scala.ex.utils.Monix._
import com.navneet.scala.ex.utils.MonadOps._

object Main {
  import com.navneet.scala.ex.utils.Helpers._
  import com.navneet.scala.ex.service.ExchangeRateService._

  def main(args: Array[String]): Unit = {
    val config = Config("http://data.fixer.io/api/latest", "9fb07e9cfd157c85a948c60aa647d955")
    val requests = Requests.empty
    type Effect[A] = EitherT[Task, AppError, A]

    implicit val exchange = Exchange.monixExcahng(config)
    implicit val console = Console.monixConsole

    implicit val requestsState = AtomicMonadState.create(Requests.empty)
    implicit val configAsk     = ApplicativeAsk.constant[Task, Config](config)

    implicit val io: SchedulerService = Scheduler.io("io-scheduler")
    implicit val show: Show[AppError] = Show.showFromToString[AppError]

    val app: Effect[Unit] = run[Effect]

    (app.run >>= {
      case -\/ (error) =>
        console.printLn(s"Encountered an Error: ${error.shows}")
      case \/- (_) => ().pure[Task]
    }).unsafeRunSync

  }

  def run[F[_]: ConfigAsk: Console: Exchange: RequestsState: ErrorHandler: Monad]: F[Unit] =
    for {
      h <- host[F]
      appkey <- appKey[F]
      _ <- Console[F].printLn(s"Using Exchange Service at host: $h and with appKey : $appkey \n")
      _ <- askFetchJudge[F].forever
    } yield ()
}
