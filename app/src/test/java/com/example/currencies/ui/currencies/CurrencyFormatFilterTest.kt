package com.example.currencies.ui.currencies

import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.lang.StringBuilder

internal class DecimalMatcherTest {

    private lateinit var decimalMatcherSUT: DecimalMatcher
    private val currencyMaxDigestBeforeDecimalPoint = 10
    private val currencyMaxDigestAfterDecimalPoint = 4


    @BeforeEach
    fun setUp() {
        decimalMatcherSUT = DecimalMatcher(
            currencyMaxDigestBeforeDecimalPoint,
            currencyMaxDigestAfterDecimalPoint
        )


    }

    @ParameterizedTest
    @ValueSource(strings = ["0.03", "2353453789.13", "1234", "."])
    fun shouldReturnTrueForCorrectInput(input: String) {
        decimalMatcherSUT = DecimalMatcher(
            maxDigitsBeforeDecimalPoint = 10,
            maxDigitsAfterDecimalPoint = 2
        )
        val result = decimalMatcherSUT.matches(input)
        val expected = true
        assertEquals(expected, result)
    }


    @ParameterizedTest
    @ValueSource(strings = [",", "s.02", "not a number"])
    fun shouldReturnFalseForNotANumberInput(input: String) {
        val result = decimalMatcherSUT.matches(input)
        val expected = false
        assertEquals(expected, result)
    }

    @Test
    fun shouldReturnFalseForTooManyDigestBeforeDecimalPoint() {
        val input = getStringLongAs(currencyMaxDigestBeforeDecimalPoint + 1, 0)
        val expected = false
        val result = decimalMatcherSUT.matches(input)
        assertEquals(expected, result)
    }

    @Test
    fun shouldReturnFalseForTooManyDigestAfterDecimalPoint() {
        val input = getStringLongAs(0, currencyMaxDigestAfterDecimalPoint + 1)
        val expected = false
        val result = decimalMatcherSUT.matches(input)
        assertEquals(expected, result)
    }

    private fun getStringLongAs(beforeDecimal: Int, afterDecimal: Int) =
        StringBuilder().apply {
            for (i in 0..beforeDecimal) {
                append("1")
            }
            append(".")
            for (i in 0..afterDecimal) {
                append("1")
            }
        }.toString()


}