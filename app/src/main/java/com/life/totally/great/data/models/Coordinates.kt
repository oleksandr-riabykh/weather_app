package com.life.totally.great.data.models

import com.google.gson.annotations.SerializedName

data class Coordinates(
    @SerializedName("lon") val lon: Double,
    @SerializedName("lat") val lat: Double
)