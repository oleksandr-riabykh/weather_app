package com.life.totally.great.di.domain

import com.life.totally.great.domain.repositories.CityRepository
import com.life.totally.great.domain.repositories.ForecastRepository
import com.life.totally.great.domain.repositories.LocationRepository
import com.life.totally.great.domain.repositories.WeatherRepository
import com.life.totally.great.domain.scheduler.LocationUpdateScheduler
import com.life.totally.great.domain.usecases.LoadCurrentLocationUseCase
import com.life.totally.great.domain.usecases.LoadWeatherUseCase
import com.life.totally.great.domain.usecases.SearchCityUseCase
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
    fun provideLoadCurrentLocationUseCase(repo: LocationRepository, scheduler: LocationUpdateScheduler) =
        LoadCurrentLocationUseCase(repo, scheduler)

    @Provides
    fun provideLoadWeatherUseCase(weatherRepo: WeatherRepository, forecastRepo: ForecastRepository) =
        LoadWeatherUseCase(weatherRepo, forecastRepo)
}