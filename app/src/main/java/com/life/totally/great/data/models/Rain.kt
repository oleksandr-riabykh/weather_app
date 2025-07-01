package com.life.totally.great.data.models

import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h") val oneHourVolume: Double
)