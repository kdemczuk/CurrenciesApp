package com.example.currencies.network

import com.example.currencies.network.currencies.CurrenciesLatestResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrenciesApi {


    companion object {
        const val BASE_URL = "https://revolut.duckdns.org/"
    }

    @GET("latest")
    suspend fun getCurrencyRates(@Query("base") base: String): CurrenciesLatestResponse
}