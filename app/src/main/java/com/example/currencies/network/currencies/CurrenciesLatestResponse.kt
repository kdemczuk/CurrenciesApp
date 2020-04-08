package com.example.currencies.network.currencies

import java.math.BigDecimal

data class CurrenciesLatestResponse(val base: String, val rates: Map<String, BigDecimal>)

