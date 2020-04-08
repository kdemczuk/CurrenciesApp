package com.example.currencies.ui.currencies

import com.example.currencies.model.currencies.CurrencyRate
import com.example.currencies.ui.base.BaseView

interface CurrenciesContract {

    interface View : BaseView {
        fun updateCurrencies(currencies: List<CurrencyRate>)
        fun scrollViewToPosition(position: Int)
        fun showError()
        fun showProgressBar()
        fun moveItem(from: Int, to: Int)
    }

    interface Presenter {
        fun onStart()
        fun onStop()
        fun retryClicked()
        fun onCurrencyClicked(currency: CurrencyRate, positionClicked: Int)
        fun onCurrencyAmountChanged(currencyPosition: Int, amount: String)
    }
}