package com.life.totally.great.data.exceptions

import retrofit2.HttpException
import java.io.IOException

fun Throwable.mapExceptionToWeatherError(): WeatherError = when (this) {
    is IOException -> WeatherError.Network(message)
    is HttpException -> if (code() == 401) {
        WeatherError.Unauthorized(message())
    } else if (code() == 404) {
        WeatherError.NotFound(message())
    } else {
        WeatherError.Unknown(message)
    }

    else -> WeatherError.Unknown(message)
}