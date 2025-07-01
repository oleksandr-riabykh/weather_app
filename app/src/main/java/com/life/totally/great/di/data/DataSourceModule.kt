package com.life.totally.great.di.data

import com.life.totally.great.data.api.ApiService
import com.life.totally.great.data.datasources.remote.city.CityRemoteDataSource
import com.life.totally.great.data.datasources.remote.city.CityRemoteDataSourceImpl
import com.life.totally.great.data.datasources.remote.forecast.ForecastRemoteDataSource
import com.life.totally.great.data.datasources.remote.forecast.ForecastRemoteDataSourceImpl
import com.life.totally.great.data.datasources.remote.weather.WeatherRemoteDataSource
import com.life.totally.great.data.datasources.remote.weather.WeatherRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideCityRemoteDataSource(
        apiService: ApiService
    ): CityRemoteDataSource = CityRemoteDataSourceImpl(apiService)

    @Provides
    @Singleton
    fun provideWeatherRemoteDataSource(
        apiService: ApiService
    ): WeatherRemoteDataSource = WeatherRemoteDataSourceImpl(apiService)

    @Provides
    @Singleton
    fun provideForecastRemoteDataSource(
        apiService: ApiService
    ): ForecastRemoteDataSource = ForecastRemoteDataSourceImpl(apiService)
}