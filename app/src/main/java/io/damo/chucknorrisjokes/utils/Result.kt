package io.damo.chucknorrisjokes.utils


sealed class Result<T> {

    abstract fun then(callback: (T) -> Unit): Result<T>
    abstract fun otherwise(errorCallback: (String) -> Unit): Result<T>

    fun always(callback: () -> Unit) = apply { callback() }


    class Success<T>(val value: T) : Result<T>() {
        override fun then(callback: (T) -> Unit) = apply {
            callback(value)
        }

        override fun otherwise(errorCallback: (String) -> Unit) = this
    }

    class Error<T>(val message: String) : Result<T>() {
        override fun then(callback: (T) -> Unit) = this

        override fun otherwise(errorCallback: (String) -> Unit) = apply {
            errorCallback(message)
        }
    }
}
