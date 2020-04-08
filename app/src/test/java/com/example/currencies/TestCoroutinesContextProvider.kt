package com.example.currencies

import com.example.currencies.util.coroutines.CoroutineContextProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.coroutines.CoroutineContext

object TestCoroutinesContextProvider : CoroutineContextProvider() {

    @ExperimentalCoroutinesApi
    override val main: CoroutineContext = Dispatchers.Unconfined

    @ExperimentalCoroutinesApi
    override val io: CoroutineContext = Dispatchers.Unconfined

}