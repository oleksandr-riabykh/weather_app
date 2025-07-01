package com.life.totally.great.utils.factories

import com.life.totally.great.data.models.GeoLocation

object GeoLocationFactory {

    fun create(
        name: String = "TestCity",
        lat: Double = 51.5074,
        lon: Double = -0.1278,
        country: String = "GB",
        state: String? = "England",
        localNames: Map<String, String>? = mapOf("en" to "TestCity")
    ): GeoLocation {
        return GeoLocation(
            name = name,
            localNames = localNames,
            lat = lat,
            lon = lon,
            country = country,
            state = state
        )
    }

    fun createList(size: Int = 3): List<GeoLocation> =
        List(size) { index ->
            create(
                name = "City$index",
                lat = 40.0 + index,
                lon = -74.0 - index,
                country = "US",
                state = "State$index"
            )
        }
}