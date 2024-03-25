package com.birthdayapp.sample.core.extensions.result

fun <T> Result<T>.onErrorReturn(fallbackSupplier: (Throwable) -> T): Result<T> =
    try {
        Result.success(getOrThrow())
    } catch (e: Exception) {
        Result.success(fallbackSupplier(e))
    }

suspend fun <T> Result<T>.onErrorReturnAsync(fallbackSupplier: suspend (Throwable) -> T): Result<T> =
    try {
        Result.success(getOrThrow())
    } catch (e: Exception) {
        Result.success(fallbackSupplier(e))
    }

