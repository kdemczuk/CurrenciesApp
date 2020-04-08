package com.example.currencies.di

import com.example.currencies.network.CurrenciesApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object NetworkModule {

    @Singleton
    @Provides
    @JvmStatic
    internal fun providesRateService(): CurrenciesApi {
        return Retrofit.Builder()
            .baseUrl(CurrenciesApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(CurrenciesApi::class.java)
    }
}