package com.example.currencies.di

import com.example.currencies.model.currencies.CurrenciesManager
import com.example.currencies.model.currencies.CurrenciesRefresherCoroutines
import com.example.currencies.ui.currencies.CurrenciesPresenter
import com.example.currencies.ui.currencies.CurrencyParser
import com.example.currencies.util.coroutines.CoroutineContextProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object CurrenciesModule {

    @Provides
    @JvmStatic
    internal fun providePresenter(
        currenciesRefresherCoroutines: CurrenciesRefresherCoroutines,
        currencyParser: CurrencyParser

    ): CurrenciesPresenter = CurrenciesPresenter(currencyParser, currenciesRefresherCoroutines)


    @Provides
    @JvmStatic
    @Singleton
    internal fun provideCurrenciesRefresherCoroutines(currenciesManager: CurrenciesManager,
                                                      coroutineContextProvider: CoroutineContextProvider
    ) = CurrenciesRefresherCoroutines(currenciesManager,coroutineContextProvider)
}