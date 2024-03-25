package com.birthdayapp.sample.core.extensions.result

val <T> Result<T>.status: ResultStatus
    get() = map { ResultStatus.SUCCESS }
        .getOrElse {
            ResultStatus.from(it.message)
        }

/**
 * @return String? returns a canonical errorMessage from [Result.Failure]
 */
val <T> Result<T>.errorMessage: String?
    get() = map { null }.getOrElse { it.message }

/**
 * Transforms the data body of the result from T to Result<R>
 *
 * Result<T>.flatTransform { data ->
 *     firstData.doSomething()
 * }
 *
 * @param T Receive type of the function
 * @param Result<R> Return type of the function
 * @param transform Defines the transformation from T to R within a lambda
 * @return Result<R> Returns a result with type Result<R>
 */
inline fun <T, R> Result<T>.flatTransform(transform: (data: T) -> Result<R>): Result<R> =
    fold(
        onSuccess = { data ->
            try {
                transform(data)
            } catch (e: Exception) {
                Result.fromException(e)
            }
        },
        onFailure = {
            Result.failure(it)
        }
    )

fun <T> Result<Result<T>>.flatten(): Result<T> =
    flatTransform { it }