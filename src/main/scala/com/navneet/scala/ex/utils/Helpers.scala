package com.navneet.scala.ex.utils

import scalaz._, Scalaz._
import com.navneet.scala.ex.models.Config
import com.navneet.scala.ex.external.AppError
import com.navneet.scala.ex.models.CurrencyModel.Currency
import com.navneet.scala.ex.models.CurrencyModel.ExchangeRate
import com.navneet.scala.ex.service.{ Console => CConsole }

trait Helpers[F[_]] {

  import CTypes._

  def host[F[_]: ConfigAsk]: F[String]
  def appKey[F[_]: ConfigAsk]: F[String]
  def currencyFromString[F[_]: Applicative: ErrorHandler](currecny: String): F[Currency]
  def lowestCurrency[F[_]: RequestsState: Functor]: F[(Currency, ExchangeRate)]
  def highestCurrency[F[_]: RequestsState: Functor]: F[(Currency, ExchangeRate)]
  def askBaseCurrencyAndExchangeCurrency[F[_]: CConsole: Monad]: F[String] =
    for {
      _ <- CConsole[F].printLn("What is the base Currecy?")
      baseCurrency <- CConsole[F].readLine
      _ <- CConsole[F].printLn("What is the Currecy for which u r looking for excahnge Rate?")
      desiredCurrency <- CConsole[F].readLine
    } yield (baseCurrency, desiredCurrency)
}

object Helpers {
  type ConfigAsk[F[_]] = ApplicativeAsk[F, Config]

  //  def host[F[_]: ConfigAsk]: F[String] = ConfigAsk[F].reader(_.host)
  //  def appKey[F[_]: ConfigAsk]: F[String] = ConfigAsk[F].reader(_.appKey)
}

object CTypes {
  type Requests = Map[Currency, ExchangeRate]
  type ConfigAsk[F[_]] = ApplicativeAsk[F, Config]
  type ErrorHandler[F[_]] = ApplicativeError[F, AppError]
  type RequestsState[F[_]] = MonadState[F, Requests]
}
