package com.example.currencies.model.currencies

import com.example.currencies.network.CurrenciesApi
import java.lang.Exception
import java.math.BigDecimal
import javax.inject.Inject

class CurrenciesManager @Inject constructor(val currenciesApi: CurrenciesApi) {

    @Throws(Exception::class)
    suspend fun getLatestCurrenciesMultiplied(base: String, baseAmount: BigDecimal): List<CurrencyRate> =
        currenciesApi.getCurrencyRates(base).let { response ->
            val currenciesRate = mutableListOf<CurrencyRate>()
            currenciesRate.add(CurrencyRate(response.base, baseAmount))
            currenciesRate.addAll(response.rates.map {
                CurrencyRate(
                    it.key,
                    it.value.multiply(baseAmount)
                )
            })
            return@let currenciesRate
        }

}