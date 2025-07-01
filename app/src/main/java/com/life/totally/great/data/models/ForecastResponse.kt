package com.life.totally.great.data.models

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("cod") val cod: String,
    @SerializedName("message") val message: Int,
    @SerializedName("cnt") val cnt: Int,
    @SerializedName("list") val forecastList: List<ForecastWeatherItem>,
    @SerializedName("city") val city: City
)