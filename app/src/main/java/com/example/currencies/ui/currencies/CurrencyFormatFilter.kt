package com.example.currencies.ui.currencies

import android.text.InputFilter
import android.text.Spanned


class CurrencyFormatFilter(
    private val decimalMatcher: DecimalMatcher
) : InputFilter {


    override fun filter(
        source: CharSequence, start: Int, end: Int,
        dest: Spanned, dstart: Int, dend: Int
    ): CharSequence? {
        val builder = StringBuilder(dest)
        builder.replace(
            dstart, dend, source
                .subSequence(start, end).toString()
        )
        return if (!decimalMatcher.matches(builder.toString())) {
            if (source.isEmpty()) dest.subSequence(dstart, dend) else ""
        } else null

    }
}

class DecimalMatcher(
    private val maxDigitsBeforeDecimalPoint: Int,
    private val maxDigitsAfterDecimalPoint: Int
) {
    fun matches(str: String): Boolean =
        str.matches(
            ("" +
                    "(([0-9]{1})([0-9]{0,"
                    + (maxDigitsBeforeDecimalPoint - 1)
                    + "})?)?(\\.[0-9]{0," +
                    maxDigitsAfterDecimalPoint +
                    "})?")
                .toRegex()
        )

}




