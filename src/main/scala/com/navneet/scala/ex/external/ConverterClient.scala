package com.navneet.scala.ex.external

import com.navneet.scala.ex.models.CurrencyModel._
import com.navneet.scala.ex.utils.CTypes.BaseCurrency

class ConverterClient(host: String, appKey: String) {
  //    def convert(currency: Currency, base: Currency, atDateTime: Long): ExchangeRate = {
  //      currency match {
  //        case AED => ExchangeRate(baseCurrency = CurrencyRate(EUR, 1.0), toCurrency = CurrencyRate(currency, 4.236854))
  //        case GBP => ExchangeRate(baseCurrency = CurrencyRate(EUR, 1.0), toCurrency = CurrencyRate(currency, 0.879153))
  //        case USD => ExchangeRate(baseCurrency = CurrencyRate(EUR, 1.0), toCurrency = CurrencyRate(currency, 1.153449))
  //        case INR => ExchangeRate(baseCurrency = CurrencyRate(EUR, 1.0), toCurrency = CurrencyRate(currency, 85.430254))
  //        case AUD => ExchangeRate(baseCurrency = CurrencyRate(EUR, 1.0), toCurrency = CurrencyRate(currency, 1.634485))
  //        case EUR => ExchangeRate(baseCurrency = CurrencyRate(EUR, 1.0), toCurrency = CurrencyRate(currency, 1.0))
  //      }
  //    }
  def convert(currency: Currency, base: BaseCurrency = Currency(EUR), atDateTime: Long): ExchangeRate = {
    currency.currency match {
      case AED => ExchangeRate(baseCurrency = base, xChngRate = 4.236854)
      case GBP => ExchangeRate(baseCurrency = base, xChngRate = 0.879153)
      case USD => ExchangeRate(baseCurrency = base, xChngRate = 1.153449)
      case INR => ExchangeRate(baseCurrency = base, xChngRate = 85.430254)
      case AUD => ExchangeRate(baseCurrency = base, xChngRate = 1.634485)
      case EUR => ExchangeRate(baseCurrency = base, xChngRate = 1.0)
    }
  }
}
