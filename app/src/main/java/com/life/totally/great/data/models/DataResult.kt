package com.life.totally.great.data.models

sealed class DataResult<out T, out E> {
    data class Success<T>(val data: T) : DataResult<T, Nothing>()
    data class Error<E>(val error: E) : DataResult<Nothing, E>()
}