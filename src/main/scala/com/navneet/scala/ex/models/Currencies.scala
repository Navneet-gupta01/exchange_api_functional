package com.navneet.scala.ex.models

import scalaz._

object CurrencyModel {
  sealed trait SupportedCurrency
  case object GBP extends SupportedCurrency
  case object USD extends SupportedCurrency
  case object INR extends SupportedCurrency
  case object AED extends SupportedCurrency
  case object AUD extends SupportedCurrency
  case object EUR extends SupportedCurrency
  implicit val currencyEqual: Equal[SupportedCurrency] = Equal.equalA

  case class Currency(currency: SupportedCurrency)


  case class ExchangeRate(baseCurrency: Currency = Currency(EUR), xChngRate: Double = 1.0)
}
