package com.navneet.scala.ex.models

object CurrencyModel {
  sealed trait Currency
  case object GBP extends Currency
  case object USD extends Currency
  case object INR extends Currency
  case object AED extends Currency
  case object AUD extends Currency
  case object EUR extends Currency

  case class ExchangeRate(baseCurrency: Currency = EUR, xChngRate: Double = 1.0)
}
