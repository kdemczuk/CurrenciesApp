package com.example.currencies.ui.currencies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.currencies.R
import com.example.currencies.model.currencies.CurrencyRate
import com.example.currencies.util.TextChangedWatcher
import com.example.currencies.util.getCurrencyFlagResId
import com.example.currencies.util.getCurrencyNameResId
import com.example.currencies.util.moveItem
import kotlinx.android.synthetic.main.currencies_list_row.view.*
import timber.log.Timber
import java.math.BigDecimal

class CurrenciesAdapter(
    private val onNewBaseCurrency: (newCurrency: CurrencyRate, position: Int) -> Unit,
    private val onNewCurrencyAmount: (position: Int, amount: String) -> Unit
) :
    Adapter<CurrenciesAdapter.CurrencyViewHolder>() {

    private val currencies = mutableListOf<CurrencyRate>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val itemView =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.currencies_list_row, parent, false)
        return CurrencyViewHolder(itemView)
    }

    override fun getItemCount(): Int = currencies.size

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bindViews(currencies[position])
    }

    override fun onBindViewHolder(
        holder: CurrencyViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            if (position == 0) {
                return
            }
            (payloads[0] as? CurrencyRate)?.let {
                holder.setCurrencyRate(it)
            }
        }
    }


    fun refreshCurrencies(newCurrencies: List<CurrencyRate>) {

        val correctOrderCurrencies = getNewCurrenciesWithCorrectOrder(newCurrencies)
        val result = DiffUtil.calculateDiff(
            CurrenciesDiffCallback(currencies, correctOrderCurrencies)
        )
        result.dispatchUpdatesTo(this)
        this.currencies.clear()
        this.currencies.addAll(correctOrderCurrencies)

    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        moveItem(currencies, fromPosition, toPosition)
    }

    private fun getNewCurrenciesWithCorrectOrder(newCurrencies: List<CurrencyRate>): List<CurrencyRate> {

        val newCurrenciesWithCorrectOrder = mutableListOf<CurrencyRate>()
        if (currencies.isEmpty()) {
            newCurrenciesWithCorrectOrder.addAll(newCurrencies)
        }
        val start = System.currentTimeMillis()

        currencies.forEach { current ->
            newCurrencies.find {
                it.symbol == current.symbol
            }?.let {
                newCurrenciesWithCorrectOrder.add(it)
            }
        }

        val end = System.currentTimeMillis()
        Timber.d("done refreshing in ${end - start}, $start | $end")

        return newCurrenciesWithCorrectOrder
    }


    inner class CurrencyViewHolder(root: View) : RecyclerView.ViewHolder(root) {

        private val currencyMaxDigestBeforeDecimalPoint = 20
        private val currencyMaxDigestAfterDecimalPoint = 2

        private val currencyChangedWatcher = TextChangedWatcher {
            onNewCurrencyAmount(adapterPosition, it)
        }

        init {
            itemView.setOnClickListener { itemView.editTextCurrency.requestFocus() }
            itemView.editTextCurrency.addTextChangedListener(currencyChangedWatcher)
            itemView.editTextCurrency.filters = arrayOf(
                CurrencyFormatFilter(
                    DecimalMatcher(
                        currencyMaxDigestBeforeDecimalPoint,
                        currencyMaxDigestAfterDecimalPoint
                    )
                )
            )
        }

        var imageViewCurrencyFlag: ImageView = root.imageViewFlag
        var textViewCurrencyShortName: TextView = root.textViewShortName
        var textViewCurrencyFullName: TextView = root.textViewFullName
        var editTextCurrency: EditText = root.editTextCurrency


        fun bindViews(rate: CurrencyRate) {

            val nameResId = getCurrencyNameResId(itemView.context, rate.symbol.toLowerCase())
            val flagResId = getCurrencyFlagResId(itemView.context, rate.symbol.toLowerCase())

            imageViewCurrencyFlag.setImageResource(flagResId)
            textViewCurrencyShortName.text = rate.symbol
            textViewCurrencyFullName.text = itemView.context.getString(nameResId)
            setCurrencyRate(rate)

        }

        fun setCurrencyRate(rate: CurrencyRate) {

            editTextCurrency.setText(rate.rate.formattedString())

            editTextCurrency.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    onNewBaseCurrency(currencies[adapterPosition], adapterPosition)
                }
            }
        }

    }

    private class CurrenciesDiffCallback(
        private val oldList: List<CurrencyRate>,
        private val newList: List<CurrencyRate>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].symbol == newList[newItemPosition].symbol


        override fun getOldListSize(): Int =
            oldList.size

        override fun getNewListSize(): Int =
            newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].rate == newList[newItemPosition].rate

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val oldRate = oldList[oldItemPosition]
            val newRate = newList[newItemPosition]
            return if (oldRate.rate != newRate.rate) newRate else null
        }

    }

}

private fun BigDecimal.formattedString(): String =
    if (compareTo(0.toBigDecimal()) == 0) "" else setScale(2, BigDecimal.ROUND_HALF_UP).toString()

