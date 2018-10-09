package com.navneet.scala.ex.service

import scalaz._
import com.navneet.scala.ex.models.CurrencyModel.{ExchangeRate, Currency}
import com.navneet.scala.ex.models.Config
import monix.eval.Task
import com.navneet.scala.ex.external.ConverterClient
import java.util.Date

trait Exchange[F[_]] {
  def exchangeRate(currency: Currency, baseCurrency: Currency): F[ExchangeRate]
}

object Exchange extends Exchange0 {
  def apply[F[_]: Exchange]: Exchange[F] = implicitly[Exchange[F]]

  def monixExcahng(config: Config): Exchange[Task] =
    new Exchange[Task] {
      val client: ConverterClient = new ConverterClient(config.host, config.appKey)
      override def exchangeRate(currency: Currency, baseCurrency: Currency): Task[ExchangeRate] = {
        Task.delay(client.convert(currency, baseCurrency, new Date().getTime))
      }
    }
}


object ExchangeForTrans {
  def forTrans[F[_]: Monad, T[_[_], _]: MonadTrans](
    implicit E: Exchange[F]): Exchange[T[F, ?]] = new Exchange[T[F, ?]] {
    def exchangeRate(desiredCurrency: Currency, baseCurrency: Currency): T[F, ExchangeRate] =
      MonadTrans[T].liftM(Exchange[F].exchangeRate(desiredCurrency, baseCurrency))  //// MonadTrans[T].liftM  lift F[A] to Monad Transformer T[F,A] here A is String since we are reading a line
  }
}

trait Exchange0 {
  implicit def eitherTExchange[F[_]: Monad: Exchange, E]: Exchange[EitherT[F, E, ?]] =
    ExchangeForTrans.forTrans[F, EitherT[?[_], E, ?]]
}
