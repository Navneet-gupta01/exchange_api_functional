package com.navneet.scala.ex.service

import com.navneet.scala.ex.models.CurrencyModel._
import scalaz._, Scalaz._
import monix.eval.Task
import monix.execution.Scheduler
import shims._
import com.navneet.scala.ex.models.Config
import com.navneet.scala.ex.external._
import cats.data.Validated
import com.navneet.scala.ex.utils.CTypes._

object ExchangeRateService {
  def getCurrency(currencyStr: String): Either[List[AppError], Currency] = currencyStr match {
    case "AED" => Right(AED)
    case "AUD" => Right(AUD)
    case "INR" => Right(INR)
    case "USD" => Right(USD)
    case "EUR" => Right(EUR)
    case "GBP" => Right(GBP)
    case _     => Left(List(UnknownCurrency(s"$currencyStr is Invalid")))
  }

  def askFetchJudge[F[_]: Console: Exchange: RequestsState: ErrorHandler: Monad] : F[Unit] =
    for {
      
    }
}
