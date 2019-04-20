package com.example.manciu.marketapp.utils

sealed class Outcome<T> {

    data class Progress<T>(var loading: Boolean) : Outcome<T>()
    data class Success<T>(var data: T) : Outcome<T>()
    data class Failure<T>(val error: Throwable) : Outcome<T>()

    companion object {
        fun <T> loading(isLoading: Boolean): Outcome<T> = Progress(isLoading)
        fun <T> success(data: T): Outcome<T> = Success(data)
        fun <T> failure(error: Throwable): Outcome<T> = Failure(error)
    }
}