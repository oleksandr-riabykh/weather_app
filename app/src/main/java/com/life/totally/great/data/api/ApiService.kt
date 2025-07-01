package com.life.totally.great.data.api

import com.life.totally.great.BuildConfig
import com.life.totally.great.data.models.ForecastResponse
import com.life.totally.great.data.models.GeoLocation
import com.life.totally.great.data.models.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

/**
 * Retrofit API interface definition using coroutines. Feel free to change this implementation to
 * suit your chosen architecture pattern and concurrency tools
 */
interface ApiService {

    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String = BuildConfig.WEATHER_API_KEY,
        @Query("lang") lang: String = Locale.getDefault().language
    ): WeatherResponse

    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String = BuildConfig.WEATHER_API_KEY
    ): ForecastResponse

    @GET("geo/1.0/direct")
    suspend fun getCitiesByName(
        @Query("q") city: String,
        @Query("limit") limit: Int = 3,
        @Query("appid") apiKey: String = BuildConfig.WEATHER_API_KEY
    ): List<GeoLocation>
}