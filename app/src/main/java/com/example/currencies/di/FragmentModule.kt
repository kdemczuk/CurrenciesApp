package com.example.currencies.di

import com.example.currencies.ui.currencies.CurrenciesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeCurrenciesFragment(): CurrenciesFragment
}
