package com.navneet.scala.ex.service

import scalaz._, Scalaz._
import com.navneet.scala.ex.utils.CTypes._
import com.navneet.scala.ex.utils.Helpers
import com.navneet.scala.ex.models.CurrencyModel._

object ExchangeRateService {
  import Helpers._
  import Exchange._

  def fetchExchangeRate[F[_]: Exchange: RequestsState: Monad](baseCurrency: Currency, desiredCurrency: Currency ): F[ExchangeRate] =
    for {
      maybeExchangeRate <- RequestsState[F].inspect(_.get(desiredCurrency))
      exchangeRate <- maybeExchangeRate.cata(
        _.pure[F],
        Exchange[F].exchangeRate(desiredCurrency,baseCurrency)
      )
      _ <- RequestsState[F].modify(_ + (desiredCurrency -> exchangeRate))
    } yield exchangeRate

  def askFetchJudge[F[_]: Console: Exchange: RequestsState: ErrorHandler: Monad]: F[Unit] =
    for {
      inputCurrencies <- askBaseCurrencyAndExchangeCurrency[F]
      exchangeRate <- fetchExchangeRate[F](inputCurrencies.baseCurrency,inputCurrencies.desiredCurrency)
      _ <- Console[F].printLn(s"Exchange Rate is 1 ${exchangeRate.baseCurrency.currency} = ${exchangeRate.xChngRate} ${inputCurrencies.desiredCurrency.currency}")
      lowestCurr <- lowestCurrency[F]
      _ <- Console[F].printLn(s"Lowest Exchange Rate calculated till now is 1 ${inputCurrencies.baseCurrency.currency} = ${lowestCurr._2} ${lowestCurr._1.currency}")
      highestCurr <- highestCurrency[F]
      _ <- Console[F].printLn(s"Highest Exchange Rate calculated till now is 1 ${inputCurrencies.baseCurrency.currency} = ${highestCurr._2} ${highestCurr._1.currency}")
    } yield ()
}
