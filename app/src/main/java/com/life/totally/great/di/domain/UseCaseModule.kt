package com.life.totally.great.di.domain

import com.life.totally.great.domain.repositories.CityRepository
import com.life.totally.great.domain.repositories.ForecastRepository
import com.life.totally.great.domain.repositories.WeatherRepository
import com.life.totally.great.domain.usecases.forecast.LoadForecastByCityUseCase
import com.life.totally.great.domain.usecases.weather.LoadWeatherByCityUseCase
import com.life.totally.great.domain.usecases.weather.LoadWeatherByCoordinatesUseCase
import com.life.totally.great.domain.usecases.search.SearchCityUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    fun provideSearchCityUseCase(repo: CityRepository) = SearchCityUseCase(repo)
    @Provides
    fun provideForecastByCityUseCase(city: CityRepository, forecast: ForecastRepository) =
        LoadForecastByCityUseCase(city, forecast)

    @Provides
    fun provideWeatherByCityUseCase(city: CityRepository, weather: WeatherRepository) =
        LoadWeatherByCityUseCase(city, weather)

    @Provides
    fun provideWeatherByCoordsUseCase(weather: WeatherRepository) =
        LoadWeatherByCoordinatesUseCase(weather)
}