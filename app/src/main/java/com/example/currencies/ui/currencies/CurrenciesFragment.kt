package com.example.currencies.ui.currencies

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencies.R
import com.example.currencies.model.currencies.CurrencyRate
import com.example.currencies.util.invisible
import com.example.currencies.util.visible
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.currencies_fragment.*
import javax.inject.Inject

class CurrenciesFragment : Fragment(), CurrenciesContract.View {


    @Inject
    lateinit var presenter: CurrenciesPresenter

    private lateinit var adapter: CurrenciesAdapter

    companion object {
        val TAG = CurrenciesFragment::class.java.simpleName
        fun newInstance(tag: String) = CurrenciesFragment()
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        presenter.attachView(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.currencies_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = CurrenciesAdapter(
            onNewBaseCurrency = { currency, position ->
                presenter.onCurrencyClicked(currency, position)

            },
            onNewCurrencyAmount = { position, value ->
                presenter.onCurrencyAmountChanged(position, value)
            })
        initViews()
    }

    override fun updateCurrencies(currencies: List<CurrencyRate>) {
        textViewError.invisible()
        progressBarLoading.invisible()
        textViewRetry.invisible()
        recyclerViewCurrencies.visible()
        adapter.refreshCurrencies(currencies)
    }

    override fun showError() {
        hideAll()
        textViewError.visible()
        textViewRetry.visible()
    }

    override fun showProgressBar() {
        hideAll()
        progressBarLoading.visible()
    }

    override fun scrollViewToPosition(position: Int) {
        recyclerViewCurrencies.scrollToPosition(position)
    }

    override fun moveItem(from: Int, to: Int) {
        adapter.moveItem(from, to)
    }

    private fun initViews() {
        recyclerViewCurrencies.layoutManager = LinearLayoutManager(context)
        recyclerViewCurrencies.adapter = adapter

        textViewRetry.setOnClickListener {
            presenter.retryClicked()
        }
    }

    private fun hideAll() {
        textViewError.invisible()
        progressBarLoading.invisible()
        textViewRetry.invisible()
        recyclerViewCurrencies.invisible()
    }


}




