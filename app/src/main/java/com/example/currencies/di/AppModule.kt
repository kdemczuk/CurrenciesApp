package com.example.currencies.di

import com.example.currencies.util.coroutines.CoroutineContextProvider
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
object AppModule {

    @Provides
    @Reusable
    @JvmStatic
    fun coroutinesContextProvider(): CoroutineContextProvider =
        CoroutineContextProvider()

}