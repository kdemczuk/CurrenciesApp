package com.example.currencies.util.coroutines

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


fun <T> debounce(
    delayMs: Long = 1000L,
    coroutineContext: CoroutineContext,
    invoke: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (debounceJob?.isCompleted != false) {
            debounceJob = CoroutineScope(coroutineContext).launch {
                invoke(param)
                delay(delayMs)
            }
        }
    }
}

fun repeat(withDelay: Long, on: CoroutineContext, action: () -> Unit): Job =
    CoroutineScope(on).launch {
        while (isActive) { // cancellable loop
            action()
            delay(withDelay)
        }
    }

fun <A, B> ifNotNull(a: A?, b: B?, code: (A, B) -> Unit) {
    if (a != null && b != null) {
        code.invoke(a, b)
    }
}

fun <A, B, C> ifNotNull(a: A?, b: B?, c: C?, code: (A, B, C) -> Unit) {
    if (a != null && b != null && c != null) {
        code.invoke(a, b, c)
    }
}