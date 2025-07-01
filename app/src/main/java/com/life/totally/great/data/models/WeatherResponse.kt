package com.life.totally.great.data.models

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("coord") val coord: Coordinates,
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("base") val base: String,
    @SerializedName("main") val main: Main,
    @SerializedName("visibility") val visibility: Int,
    @SerializedName("wind") val wind: Wind,
    @SerializedName("rain") val rain: Rain?,
    @SerializedName("clouds") val clouds: Clouds,
    @SerializedName("dt") val datetime: Long,
    @SerializedName("sys") val sys: Sys,
    @SerializedName("timezone") val timezone: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val cityName: String,
    @SerializedName("cod") val cod: Int
)