package com.life.totally.great.data.exceptions

/**
 * can be added more exception types,
 * Like Timeout, File not found etc.
 */
sealed class WeatherError(val code: ErrorCode, val message: String?) {
    class NoLocation(message: String?) : WeatherError(ErrorCode.NO_LOCATION, message)
    class Network(message: String?) : WeatherError(ErrorCode.NETWORK, message)
    class NotFound(message: String?) : WeatherError(ErrorCode.NOT_FOUND, message)
    class Unauthorized(message: String?) : WeatherError(ErrorCode.UNAUTHORIZED, message)
    class Unknown(message: String?) : WeatherError(ErrorCode.UNKNOWN, message)
}