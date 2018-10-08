package com.navneet.scala.ex.utils

import scalaz.{MonadState => _, _}
import Scalaz._
import com.navneet.scala.ex.models.{Config, InputCurrencies}
import com.navneet.scala.ex.external.{AppError, UnSupportedBaseCurrency, UnknownCurrency}
import com.navneet.scala.ex.models.CurrencyModel._
import com.navneet.scala.ex.service.{Console => CConsole}
import mtl._

trait Helpers[F[_]] {
  import CTypes._
  def host[F[_]: ConfigAsk]: F[String]
  def appKey[F[_]: ConfigAsk]: F[String]
  def currencyFromString[F[_]: Applicative: ErrorHandler](currency: String): F[Currency]
  def lowestCurrency[F[_]: RequestsState: Functor]: F[(Currency, ExchangeRate)]
  def highestCurrency[F[_]: RequestsState: Functor]: F[(Currency, ExchangeRate)]
  def askBaseCurrencyAndExchangeCurrency[F[_]: CConsole: Monad: ErrorHandler]: F[String]
}

object Helpers{
  import CTypes._

  def host[F[_]: ConfigAsk]: F[String] = ConfigAsk[F].reader(_.host)
  def appKey[F[_]: ConfigAsk]: F[String] = ConfigAsk[F].reader(_.appKey)

  def currencyFromString[F[_]: Applicative: ErrorHandler](currencyStr: String): F[Currency] = currencyStr match {
    case "AED" => Currency(AED).pure[F]
    case "AUD" => Currency(AUD).pure[F]
    case "INR" => Currency(INR).pure[F]
    case "USD" => Currency(USD).pure[F]
    case "EUR" => Currency(EUR).pure[F]
    case "GBP" => Currency(GBP).pure[F]
    case _ => ErrorHandler[F].raiseError(UnknownCurrency(s"$currencyStr is Invalid"))
  }

  def validateSupportedBaseCurrency[F[_]: Applicative: ErrorHandler](currency: Currency): F[Currency] = currency.currency match {
    case EUR => currency.pure[F]
    case _ => ErrorHandler[F].raiseError(UnSupportedBaseCurrency(currency.currency ))
  }

  type RateForExchange = Double

  def lowestCurrency[F[_]: RequestsState: Functor]: F[(Currency, RateForExchange)] = for {
    results <- RequestsState[F].inspect(
      reqs =>
        reqs.toList
          .sortBy(_._2.xChngRate)
          .map {
            case (desiredCurrecny, exchangeRate) => (desiredCurrecny, exchangeRate.xChngRate)
          }
          .reverse)
  } yield results(0)


  def highestCurrency[F[_]: RequestsState: Functor]: F[(Currency, RateForExchange)] = for {
    results <- RequestsState[F].inspect(
      reqs =>
        reqs.toList
          .sortBy(_._2.xChngRate)
          .map {
            case (desiredCurrecny, exchangeRate) => (desiredCurrecny, exchangeRate.xChngRate)
          })
  } yield results(0)


  def askBaseCurrencyAndExchangeCurrency[F[_]: CConsole: Monad: ErrorHandler]: F[InputCurrencies] =
    for {
      _ <- CConsole[F].printLn("Please Enter Base Currency? ")
      baseCurrencyName <- CConsole[F].readLine
      baseCurrency <- currencyFromString[F](baseCurrencyName)
      _ <- validateSupportedBaseCurrency[F](baseCurrency)
      _ <- CConsole[F].printLn("Please Enter Desired Currency For Exchange? ")
      desiredCurrencyName <- CConsole[F].readLine
      desiredCurrency <- currencyFromString[F](desiredCurrencyName)
    } yield (InputCurrencies(baseCurrency,desiredCurrency))
}


object CTypes {

  type Requests = Map[Currency, ExchangeRate]
  object Requests {
    def empty: Requests = Map.empty[Currency, ExchangeRate]
  }

  type ConfigAsk[F[_]] = ApplicativeAsk[F, Config]
  object ConfigAsk {
    def apply[F[_]: ConfigAsk]: ConfigAsk[F] = implicitly[ApplicativeAsk[F, Config]]
  }

  type ErrorHandler[F[_]] = ApplicativeError[F, AppError]
  object ErrorHandler {
    def apply[F[_]: ErrorHandler]: ErrorHandler[F] =
      implicitly[ApplicativeError[F, AppError]]
  }

  type RequestsState[F[_]] = MonadState[F, Requests]
  object RequestsState {
    def apply[F[_]: RequestsState]: RequestsState[F] =
      implicitly[MonadState[F, Requests]]
  }

  type BaseCurrency = Currency
  type DesiredCurrency = Currency

}