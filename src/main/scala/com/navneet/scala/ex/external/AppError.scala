package com.navneet.scala.ex.external

import com.navneet.scala.ex.models.CurrencyModel.Currency

sealed trait AppError
case class UnknownCurrency(currency: String) extends AppError
case class UnSupportedBaseCurrency(currency: Currency) extends AppError

sealed trait ServiceError
case object ServiceNotAvailable extends ServiceError
