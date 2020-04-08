package com.example.currencies.ui.currencies

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.math.BigDecimal

internal class CurrencyParserTest {

    lateinit var currencyParserSUT: CurrencyParser

    @BeforeEach
    fun setup() {
        currencyParserSUT = CurrencyParser()
    }

    @Test
    fun shouldReturnZeroWhenInputIsEmpty() {
        val expected = BigDecimal.ZERO
        val actual = currencyParserSUT.getValue("")
        assertEquals(expected, actual)
    }

    @Test
    fun shouldReturnZeroWhenInputIsOnlyDotDelimiter() {
        val expected = BigDecimal.ZERO
        val actual = currencyParserSUT.getValue(",")
        assertEquals(expected, actual)
    }

    @Test
    fun shouldReturnZeroWhenInputIsNotANumber() {
        val expected = BigDecimal.ZERO
        val actual = currencyParserSUT.getValue("not a number input")
        assertEquals(expected, actual)
    }

    @Test
    fun shouldReturnZeroWhenInputIsOnlyCommaDelimiter() {
        val expected = BigDecimal.ZERO
        val actual = currencyParserSUT.getValue(".")
        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @ValueSource(strings = ["0.0003", "3.1345", "1234.56789"])
    fun shouldReturnCorrectValueWhenInputIsANumberWithDot(input: String) {
        val expected = BigDecimal(input)
        val result = currencyParserSUT.getValue(input)
        assertEquals(expected, result)
    }

    @ParameterizedTest
    @ValueSource(strings = ["0,0003", "3,1345", "1234,56789"])
    fun shouldReturnCorrectValueWhenInputIsANumberWithComma(input: String) {
        val expected = BigDecimal(input.replace(",","."))
        val result = currencyParserSUT.getValue(input)
        assertEquals(expected, result)
    }

}