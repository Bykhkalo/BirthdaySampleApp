package com.birthdayapp.sample.core.extensions.result

import java.io.IOException
import java.io.InterruptedIOException
import java.net.*
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeoutException

fun <T> Result.Companion.fromException(
    throwable: Throwable,
): Result<T> =
    when (throwable) {
        is CancellationException -> Result.canceled<T>(throwable)
        is UnknownHostException, is ConnectException -> Result.networkError(throwable)
        is SocketTimeoutException -> Result.timeoutError(throwable)
        is InterruptedIOException -> Result.timeoutError(throwable)
        is TimeoutException -> Result.timeoutError(throwable)
        is IOException -> Result.ioError(throwable)
        else -> Result.generalError(throwable)
    }
        .also {
            if (shouldLogError(throwable)) {
                //Timber.d("Unexpected error in [Result]", throwable)
            }
        }

private fun shouldLogError(throwable: Throwable): Boolean = true
