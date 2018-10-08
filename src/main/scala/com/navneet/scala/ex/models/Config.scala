package com.navneet.scala.ex.models

import com.navneet.scala.ex.models.CurrencyModel.Currency

case class Config(host: String, appKey: String)
case class InputCurrencies(baseCurrency: Currency, desiredCurrency: Currency )