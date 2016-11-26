package io.damo.chucknorrisjokes.utils

sealed class Result<T> {
    class Success<T>(val value: T) : Result<T>()
    class Error<T>(val message: String) : Result<T>()
}
