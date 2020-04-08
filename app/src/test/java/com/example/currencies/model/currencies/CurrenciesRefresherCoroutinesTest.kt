package com.example.currencies.model.currencies

import com.example.currencies.TestCoroutinesContextProvider
import com.example.currencies.util.coroutines.CoroutineContextProvider
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.lang.Exception
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
internal class CurrenciesRefresherCoroutinesTest {


    @Mock
    lateinit var currenciesManagerMock: CurrenciesManager

    @Mock
    lateinit var newCurrenciesCallbackMock: CurrenciesRefresher.Callback

    private lateinit var currenciesRefresherCoroutinesSUT: CurrenciesRefresherCoroutines

    private lateinit var contextProvider: CoroutineContextProvider

    @BeforeEach
    fun setUp() {
        contextProvider = spy(TestCoroutinesContextProvider)

        currenciesRefresherCoroutinesSUT = CurrenciesRefresherCoroutines(
            currenciesManager = currenciesManagerMock,
            coroutineContextProvider = contextProvider,
            newCurrenciesCallback = newCurrenciesCallbackMock

        )
    }

    @ExperimentalCoroutinesApi
    @Nested
    inner class OnRefreshCurrenciesInvoked {

        private val amountToStartRefreshing = BigDecimal.ONE
        private val currencyBaseToStartRefreshing = "AUD"
        private val refreshIntervalMillis = 2000L
        private val retryOnError = true

        @Nested
        inner class WithSuccess {
            private val latestCurrencies = listOf<CurrencyRate>()

            @BeforeEach
            fun startRefreshingCurrencies() {
                runBlockingTest {
                    whenever(
                        currenciesManagerMock.getLatestCurrenciesMultiplied(
                            currencyBaseToStartRefreshing,
                            amountToStartRefreshing
                        )
                    ).thenReturn(latestCurrencies)
                }

                currenciesRefresherCoroutinesSUT.startRefreshingCurrencies(
                    amountToStartRefreshing,
                    currencyBaseToStartRefreshing,
                    refreshIntervalMillis,
                    retryOnError
                )
            }

            @Test
            fun shouldRequestLatestCurrencies() {
                verify(contextProvider).io
                runBlockingTest {
                    verify(currenciesManagerMock).getLatestCurrenciesMultiplied(
                        base = eq(currencyBaseToStartRefreshing),
                        baseAmount = eq(amountToStartRefreshing)
                    )
                }
            }

            @Test
            fun shouldCallBackWithCurrencies() {
                verify(contextProvider).main
                verify(newCurrenciesCallbackMock).newCurrencies(latestCurrencies)
            }
        }


        @Nested
        inner class WithError {

            @BeforeEach
            fun withError(){
                runBlockingTest {
                    doThrow(Exception())
                        .`when`(currenciesManagerMock).getLatestCurrenciesMultiplied(
                            currencyBaseToStartRefreshing,
                            amountToStartRefreshing)
                }
            }

            @Nested
            inner class RetryOnError{
                @BeforeEach
                fun startRefreshingCurrencies() {
                    currenciesRefresherCoroutinesSUT.startRefreshingCurrencies(
                        amountToStartRefreshing,
                        currencyBaseToStartRefreshing,
                        refreshIntervalMillis,
                        true
                    )
                }

                @Test
                fun shouldRefresherBeActive() {
                    assertEquals(true, currenciesRefresherCoroutinesSUT.isActive())
                }

                @Test
                fun shouldCallBackWithError() {
                    verify(contextProvider).main
                    verify(newCurrenciesCallbackMock).onError(any())

                }
            }
            @Nested
            inner class DoNotRetryOnError{
                @BeforeEach
                fun startRefreshingCurrencies() {
                    currenciesRefresherCoroutinesSUT.startRefreshingCurrencies(
                        amountToStartRefreshing,
                        currencyBaseToStartRefreshing,
                        refreshIntervalMillis,
                        false
                    )
                }

                @Test
                fun shouldRefresherBeActive() {
                    assertEquals(false, currenciesRefresherCoroutinesSUT.isActive())
                }

                @Test
                fun shouldCallBackWithError() {
                    verify(contextProvider).main
                    verify(newCurrenciesCallbackMock).onError(any())

                }
            }


        }
    }

    @Nested
    inner class OnStopInvoked{

        @BeforeEach
        fun onStop(){
            currenciesRefresherCoroutinesSUT.stop()
        }

        @Test
        fun shouldStopRefreshing(){
            assertEquals(false,currenciesRefresherCoroutinesSUT.isActive())
        }
    }

    @AfterEach
    fun tearDown() {
    }
}