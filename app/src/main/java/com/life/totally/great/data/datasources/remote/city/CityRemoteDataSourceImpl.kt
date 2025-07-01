package com.life.totally.great.data.datasources.remote.city

import com.life.totally.great.data.api.ApiService
import com.life.totally.great.data.models.GeoLocation
import javax.inject.Inject

class CityRemoteDataSourceImpl @Inject constructor(
    private val api: ApiService
) : CityRemoteDataSource {

    override suspend fun getCitiesByName(name: String): List<GeoLocation> {
        return api.getCitiesByName(name)
    }
}