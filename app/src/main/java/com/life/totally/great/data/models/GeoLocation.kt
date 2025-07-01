package com.life.totally.great.data.models

import com.google.gson.annotations.SerializedName

data class GeoLocation(
    @SerializedName("name") val name: String,
    @SerializedName("local_names") val localNames: Map<String, String>? = null,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
    @SerializedName("country") val country: String,
    @SerializedName("state") val state: String? = null
)
