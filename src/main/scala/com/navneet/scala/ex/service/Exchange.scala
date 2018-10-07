package com.navneet.scala.ex.service

import com.navneet.scala.ex.models.CurrencyModel.{Currency, ExchangeRate}

trait Exchange[F[_]] {
  type BaseCurrency = Currency
  
  def exchangeRate(currency: Currency, baseCurrency: BaseCurrency): F[ExchangeRate]
}