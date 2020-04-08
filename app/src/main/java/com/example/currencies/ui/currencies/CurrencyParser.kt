package com.example.currencies.ui.currencies

import java.math.BigDecimal
import javax.inject.Inject

class CurrencyParser @Inject constructor() {

    fun getValue(amount: String): BigDecimal {

        if (amount.isBlank()) return BigDecimal.ZERO
        val replaced = amount.replace(",", ".")
        if (replaced.toDoubleOrNull() == null) return BigDecimal.ZERO
        return BigDecimal(replaced)
    }

}