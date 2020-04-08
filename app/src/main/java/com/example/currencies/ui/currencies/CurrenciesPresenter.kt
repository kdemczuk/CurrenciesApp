package com.example.currencies.ui.currencies

import com.example.currencies.model.currencies.CurrenciesRefresher
import com.example.currencies.model.currencies.CurrencyRate
import com.example.currencies.ui.base.BasePresenter
import java.math.BigDecimal
import javax.inject.Inject

class CurrenciesPresenter @Inject constructor(

    private val currencyParser: CurrencyParser,
    private val currenciesRefresher: CurrenciesRefresher
) :
    BasePresenter<CurrenciesContract.View>(), CurrenciesContract.Presenter {

    companion object {
        const val currencyListPrivilegedPosition = 0
        const val refreshCurrenciesRateMillis = 1000L
        const val defaultCurrenciesBase = "EUR"
        val defaultRefreshAmount: BigDecimal = BigDecimal.ONE
    }

    private var currencyBase = defaultCurrenciesBase
    private var currencyAmount = defaultRefreshAmount

    init {
        currenciesRefresher.newCurrenciesCallback =
            object : CurrenciesRefresher.Callback {
                override fun newCurrencies(currencies: List<CurrencyRate>) {
                    view?.updateCurrencies(currencies)
                }

                override fun onError(e: Exception) {
                    view?.showError()

                }

            }
    }

    override fun onStart() {
        view?.showProgressBar()
        refreshCurrencies(defaultRefreshAmount)
    }

    override fun onStop() {
        currenciesRefresher.stop()
    }

    private fun refreshCurrencies(newAmount: BigDecimal) {
        currencyAmount = newAmount
        currenciesRefresher.startRefreshingCurrencies(
            newAmount,
            currencyBase,
            refreshCurrenciesRateMillis,
            true
        )
    }

    override fun retryClicked() {
        view?.showProgressBar()
        refreshCurrencies(currencyAmount)
    }

    override fun onCurrencyClicked(currency: CurrencyRate, positionClicked: Int) {
        view?.scrollViewToPosition(currencyListPrivilegedPosition)
        currencyBase = currency.symbol
        refreshCurrencies(currency.rate)
        view?.moveItem(positionClicked, currencyListPrivilegedPosition)
    }

    override fun onCurrencyAmountChanged(currencyPosition: Int, amount: String) {
        if (currencyPosition == currencyListPrivilegedPosition)
            refreshCurrencies(currencyParser.getValue(amount))
    }


}




