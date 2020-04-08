package com.example.currencies.util

import android.content.Context

fun getCurrencyNameResId(context: Context, symbol: String) =
    context.resources.getIdentifier(
        "curr_${symbol}_name", "string",
        context.packageName
    )

fun getCurrencyFlagResId(context: Context, symbol: String) = context.resources.getIdentifier(
    "ic_$symbol", "drawable", context.packageName
)