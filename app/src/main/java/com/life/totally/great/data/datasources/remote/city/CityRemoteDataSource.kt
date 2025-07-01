package com.life.totally.great.data.datasources.remote.city

import com.life.totally.great.data.models.GeoLocation

interface CityRemoteDataSource {
    suspend fun getCitiesByName(name: String): List<GeoLocation>
}