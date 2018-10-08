package com.navneet.scala.ex.external

import com.navneet.scala.ex.models.CurrencyModel.SupportedCurrency

sealed trait AppError
case class UnknownCurrency(currency: String) extends AppError
case class UnSupportedBaseCurrency(currency: SupportedCurrency) extends AppError

sealed trait ServiceError
case object ServiceNotAvailable extends ServiceError
