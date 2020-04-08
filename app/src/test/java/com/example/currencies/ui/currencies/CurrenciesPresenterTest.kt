package com.example.currencies.ui.currencies

import com.example.currencies.model.currencies.CurrenciesRefresherCoroutines
import com.example.currencies.model.currencies.CurrencyRate
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
internal class CurrenciesPresenterTest {

    val currencyListPrivilegedPosition = CurrenciesPresenter.currencyListPrivilegedPosition
    val refreshCurrenciesRateMillis = CurrenciesPresenter.refreshCurrenciesRateMillis
    val defaultCurrenciesBase = CurrenciesPresenter.defaultCurrenciesBase
    val defaultRefreshAmount: BigDecimal = CurrenciesPresenter.defaultRefreshAmount
    val retryOnError = true

    @Mock
    lateinit var currencyParserMock: CurrencyParser

    @Mock
    lateinit var currenciesRefresherMock: CurrenciesRefresherCoroutines

    @Mock
    lateinit var currenciesViewMock: CurrenciesContract.View

    private lateinit var currenciesPresenterSUT: CurrenciesPresenter

    @BeforeEach
    fun setUp() {
        currenciesPresenterSUT = CurrenciesPresenter(currencyParserMock, currenciesRefresherMock)
        currenciesPresenterSUT.attachView(currenciesViewMock)
    }

    @Nested
    inner class OnAttachedInvoked {
        @BeforeEach
        fun onAttach() {

        }
    }

    @Nested
    inner class OnStartInvoked {
        @BeforeEach
        fun onStart() {
            currenciesPresenterSUT.onStart()
        }

        @Test
        fun shouldRequestProgressOnView() {
            verify(currenciesViewMock).showProgressBar()
        }

        @Test
        fun shouldRequestRefreshCurrencies() {
            verify(currenciesRefresherMock).startRefreshingCurrencies(
                eq(defaultRefreshAmount),
                eq(defaultCurrenciesBase),
                eq(refreshCurrenciesRateMillis),
                eq(retryOnError)
            )
        }


    }

    @Nested
    inner class OnStopInvoked {
        @BeforeEach
        fun onStop() {
            currenciesPresenterSUT.onStop()
        }

        @Test
        fun shouldRequestRefreshCancelling() {
            verify(currenciesRefresherMock).stop()
        }
    }

    @Nested
    inner class OnRetryInvoked {
        @BeforeEach
        fun retryClicked() {
            currenciesPresenterSUT.retryClicked()
        }

        @Test
        fun shouldRequestProgressOnView() {
            verify(currenciesViewMock).showProgressBar()
        }

        @Test
        fun shouldRequestRefreshCurrencies() {
            verify(currenciesRefresherMock).startRefreshingCurrencies(
                eq(defaultRefreshAmount),
                eq(defaultCurrenciesBase),
                eq(refreshCurrenciesRateMillis),
                eq(retryOnError)
            )
        }

    }

    @Nested
    inner class OnCurrencyClickedInvoked {
        private val currencyRate = CurrencyRate("EUR", BigDecimal.ONE)
        private val positionClicked = 1
        @BeforeEach
        fun onCurrencyClicked() {
            currenciesPresenterSUT.onCurrencyClicked(currencyRate, positionClicked)
        }

        @Test
        fun shouldRequestScrollOnView() {
            verify(currenciesViewMock).scrollViewToPosition(
                eq(currencyListPrivilegedPosition)
            )
        }

        @Test
        fun shouldRequestRefreshCurrencies() {
            verify(currenciesRefresherMock).startRefreshingCurrencies(
                eq(currencyRate.rate),
                eq(currencyRate.symbol),
                eq(refreshCurrenciesRateMillis),
                eq(retryOnError)
            )
        }

        @Test
        fun shouldRequestMoveItemOnView() {
            verify(currenciesViewMock).moveItem(
                eq(positionClicked),
                eq(currencyListPrivilegedPosition)
            )
        }
    }

    @Nested
    inner class OnCurrencyAmountChangedInvoked {

        @Nested
        inner class WithPrivilegedPosition {
            private val changedCurrencyListPosition = currencyListPrivilegedPosition
            private val newAmount = "123.45"
            private val newAmountBD = BigDecimal(newAmount)

            @BeforeEach
            fun onCurrencyAmountChanged() {
                whenever(currencyParserMock.getValue(newAmount)).thenReturn(newAmountBD)
                currenciesPresenterSUT.onCurrencyAmountChanged(
                    changedCurrencyListPosition,
                    newAmount
                )
            }

            @Test
            fun shouldRefreshCurrenciesWithNewAmount() {
                verify(currenciesRefresherMock).startRefreshingCurrencies(
                    eq(newAmountBD),
                    eq(defaultCurrenciesBase),
                    eq(refreshCurrenciesRateMillis),
                    eq(retryOnError)
                )
            }

            @Test
            fun shouldRequestParseValue() {
                verify(currencyParserMock).getValue(
                    eq(newAmount)
                )
            }

        }

        @Nested
        inner class WithNotPrivilegedPosition {
            private val changedCurrencyListPosition = currencyListPrivilegedPosition + 1
            private val newAmount = "123.45"
            @BeforeEach
            fun onCurrencyAmountChanged() {

                currenciesPresenterSUT.onCurrencyAmountChanged(
                    changedCurrencyListPosition,
                    newAmount
                )
            }

            @Test
            fun shouldNotRefreshCurrenciesWithNewAmount() {
                verify(currenciesRefresherMock, never()).startRefreshingCurrencies(
                    any(),
                    ArgumentMatchers.anyString(),
                    ArgumentMatchers.anyLong(),
                    any()

                )
            }


            @Test
            fun shouldNotRequestParseValue() {
                verifyZeroInteractions(currencyParserMock)
            }
        }

    }


}