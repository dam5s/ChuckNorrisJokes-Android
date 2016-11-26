package io.damo.chucknorrisjokes.utils

import io.damo.chucknorrisjokes.utils.Result.Success

sealed class Result<T> {
    class Success<T>(val value: T) : Result<T>()
    class Error<T>(val message: String) : Result<T>()
}

fun <T> Result<T>.then(callback: (T) -> Unit): Result<T> {
    when (this) {
        is Success -> callback(this.value)
    }

    return this
}
