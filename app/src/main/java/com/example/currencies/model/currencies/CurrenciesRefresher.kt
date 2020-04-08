package com.example.currencies.model.currencies

import com.example.currencies.util.coroutines.CoroutineContextProvider
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Exception
import java.math.BigDecimal
import javax.inject.Inject

interface CurrenciesRefresher {
    var newCurrenciesCallback: Callback?
    fun startRefreshingCurrencies(
        newAmount: BigDecimal,
        currencyBase: String,
        refreshCurrenciesRateMillis: Long,
        retryOnError :Boolean
    )

    fun isActive() : Boolean

    interface Callback {
        fun newCurrencies(currencies: List<CurrencyRate>)
        fun onError(e: Exception)
    }

    fun stop()
}

class CurrenciesRefresherCoroutines @Inject constructor(
    private val currenciesManager: CurrenciesManager,
    private val coroutineContextProvider: CoroutineContextProvider,
    override var newCurrenciesCallback: CurrenciesRefresher.Callback? = null
) : CurrenciesRefresher {

    private var refreshCurrenciesJob: Job? = null

    override fun startRefreshingCurrencies(
        newAmount: BigDecimal,
        currencyBase: String,
        refreshCurrenciesRateMillis: Long,
        retryOnError: Boolean
    ) {
        refreshCurrenciesJob?.cancel()
        refreshCurrenciesJob = CoroutineScope(coroutineContextProvider.io).launch {
            while (isActive) { // cancellable loop
                try {
                    val currencies =
                        currenciesManager.getLatestCurrenciesMultiplied(currencyBase, newAmount)
                    Timber.d("currencies updated, base $currencyBase , amount :$newAmount")
                    withContext(coroutineContextProvider.main) {
                        newCurrenciesCallback?.newCurrencies(currencies)
                    }
                    delay(refreshCurrenciesRateMillis)
                } catch (e: Exception) {
                    Timber.d("get latest currencies error $e")
                    withContext(coroutineContextProvider.main) {
                        newCurrenciesCallback?.onError(e)
                    }
                    if(retryOnError){
                        delay(refreshCurrenciesRateMillis)
                        startRefreshingCurrencies(
                            newAmount,
                            currencyBase,
                            refreshCurrenciesRateMillis,
                            retryOnError)
                    }
                    cancel()

                }

            }
        }
    }

    override fun stop() {
        refreshCurrenciesJob?.cancel()
    }

    override fun isActive() = refreshCurrenciesJob?.isActive ?: false


}