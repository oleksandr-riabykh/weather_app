package com.life.totally.great.domain.usecases.search

import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.data.models.GeoLocation
import com.life.totally.great.domain.repositories.CityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchCityUseCase @Inject constructor(
    private val cityRepository: CityRepository
) {
    suspend operator fun invoke(name: String): Flow<DataResult<List<GeoLocation>, WeatherError>> {
        return cityRepository.searchCity(name)
    }
}