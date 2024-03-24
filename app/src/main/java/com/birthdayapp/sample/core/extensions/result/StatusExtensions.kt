package com.birthdayapp.sample.core.extensions.result

import java.io.IOException
import java.net.ConnectException

/**
 * Creates a new Result from status general error
 *
 * @param T Type of the result data body
 * @param cause Stacktrace of the error
 * @return Result<T> Returns a result with type <T>
 */
fun <T> Result.Companion.generalError(
    cause: Throwable,
): Result<T> {
    val throwable = Throwable(
        message = ResultStatus.GENERAL_ERROR.name,
        cause = cause,
    )
    return failure(throwable)
}

/**
 * Creates a new Result from status general error
 *
 * @param T Type of the result data body
 * @param message Message of the error
 * @return Result<T> Returns a result with type <T>
 */
fun <T> Result.Companion.generalError(
    message: String
): Result<T> {
    val throwable = Throwable(
        message = ResultStatus.GENERAL_ERROR.name,
        cause = IllegalStateException(message),
    )
    return failure(throwable)
}

/**
 * Creates a new Result from status user error
 *
 * @param T Type of the results data body
 * @param message Message of the error
 * @return Result<T> Returns a result with type <T>
 */
fun <T> Result.Companion.userError(
    message: String,
): Result<T> {
    val throwable = Throwable(
        message = ResultStatus.USER_ERROR.name,
        cause = IllegalStateException(message),
    )
    return failure(throwable)
}

/**
 * Creates a new Result from status authorization error
 *
 * @param T Type of the result data body
 * @param message Message of the error
 * @return Result<T> Returns a result with type <T>
 */
fun <T> Result.Companion.authorizationError(
    message: String,
): Result<T> {
    val throwable = Throwable(
        message = ResultStatus.AUTHORIZATION_ERROR.name,
        cause = SecurityException(message),
    )
    return failure(throwable)
}

/**
 * Creates a new Result from status not-found
 *
 * @param T Type of the results data body
 * @param message Message of the error
 * @return Result<T> Returns a result with type <T>
 */
fun <T> Result.Companion.notFound(
    message: String,
): Result<T> {
    val throwable = Throwable(
        message = ResultStatus.NOT_FOUND.name,
        cause = IOException(message),
    )
    return failure(throwable)
}

/**
 * Creates a new Result from status customer already exists
 *
 * @param T Type of the results data body
 * @param message Message of the error
 * @return Result<T> Returns a result with type <T>
 */
fun <T> Result.Companion.resourceConflictError(
    message: String,
): Result<T> {
    val throwable = Throwable(
        message = ResultStatus.RESOURCE_CONFLICT.name,
        cause = IOException(message),
    )
    return failure(throwable)
}

/**
 * Creates a new Result from status network error
 *
 * @param T Type of the results data body
 * @param message Message of the error
 * @return Result<T> Returns a result with type <T>
 */
fun <T> Result.Companion.networkError(
    message: String,
): Result<T> {
    val throwable = Throwable(
        message = ResultStatus.NETWORK_ERROR.name,
        cause = ConnectException(message),
    )
    return failure(throwable)
}

/**
 * Creates a new Result from status network error
 *
 * @param T Type of the results data body
 * @param cause Stacktrace of the error
 * @return Result<T> Returns a result with type <T>
 */
fun <T> Result.Companion.networkError(
    cause: Throwable,
): Result<T> {
    val throwable = Throwable(
        message = ResultStatus.NETWORK_ERROR.name,
        cause = cause,
    )
    return failure(throwable)
}

/**
 * Creates a new Result from status canceled
 *
 * @param T Type of the results data body
 * @param cause Stacktrace of the error
 * @return Result<T> Returns a result with type <T>
 */
fun <T> Result.Companion.canceled(
    cause: Throwable,
): Result<T> {
    val throwable = Throwable(
        message = ResultStatus.CANCELED.name,
        cause = cause,
    )
    return failure(throwable)
}

/**
 * Creates a new Result from status timeout error
 *
 * @param T Type of the results data body
 * @param cause Stacktrace of the error
 * @return Result<T> Returns a result with type <T>
 */
fun <T> Result.Companion.timeoutError(
    cause: Throwable,
): Result<T> {
    val throwable = Throwable(
        message = ResultStatus.TIMEOUT_ERROR.name,
        cause = cause,
    )
    return failure(throwable)
}

/**
 * Creates a new Result from status io error
 *
 * @param T Type of the results data body
 * @param cause Stacktrace of the error
 * @return Result<T> Returns a result with type <T>
 */
fun <T> Result.Companion.ioError(
    cause: Throwable,
): Result<T> {
    val throwable = Throwable(
        message = ResultStatus.IO_ERROR.name,
        cause = cause,
    )
    return failure(throwable)
}
