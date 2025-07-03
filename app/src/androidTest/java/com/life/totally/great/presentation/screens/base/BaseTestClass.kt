package com.life.totally.great.presentation.screens.base

import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.models.Coordinates
import com.life.totally.great.utils.factories.ForecastResponseFactory
import com.life.totally.great.utils.factories.GeoLocationFactory

open class BaseTestClass {
    val mockTestCityName = "SearchTest"
    val mockResponseCityName = "testonto"
    val mockErrorMessage = "Network error"
    val mockCoordinates = Coordinates(-74.0, 40.0)
    val mockCity =
        GeoLocationFactory.create(
            name = mockTestCityName,
            lat = mockCoordinates.lat,
            lon = mockCoordinates.lon
        )
    val mockCities = listOf(mockCity, mockCity)
    val mockResponseCity = ForecastResponseFactory.createCity(name = mockResponseCityName)
    val mockTestForecastResponse = ForecastResponseFactory.create(city = mockResponseCity)
    val testError = WeatherError.Unknown(mockErrorMessage)
}