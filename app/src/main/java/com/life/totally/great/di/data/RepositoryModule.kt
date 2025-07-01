package com.life.totally.great.di.data

import com.life.totally.great.data.datasources.local.location.LocationDataSource
import com.life.totally.great.data.datasources.remote.city.CityRemoteDataSource
import com.life.totally.great.data.datasources.remote.forecast.ForecastRemoteDataSource
import com.life.totally.great.data.datasources.remote.weather.WeatherRemoteDataSource
import com.life.totally.great.data.repositores.CityRepositoryImpl
import com.life.totally.great.data.repositores.ForecastRepositoryImpl
import com.life.totally.great.data.repositores.LocationRepositoryImpl
import com.life.totally.great.data.repositores.WeatherRepositoryImpl
import com.life.totally.great.domain.repositories.CityRepository
import com.life.totally.great.domain.repositories.ForecastRepository
import com.life.totally.great.domain.repositories.LocationRepository
import com.life.totally.great.domain.repositories.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCityRepository(
        dataSource: CityRemoteDataSource,
        dispatcher: CoroutineDispatcher
    ): CityRepository = CityRepositoryImpl(dataSource, dispatcher)

    @Provides
    @Singleton
    fun provideWeatherRepository(
        dataSource: WeatherRemoteDataSource,
        dispatcher: CoroutineDispatcher
    ): WeatherRepository = WeatherRepositoryImpl(dataSource, dispatcher)

    @Provides
    @Singleton
    fun provideForecastRepository(
        dataSource: ForecastRemoteDataSource,
        dispatcher: CoroutineDispatcher
    ): ForecastRepository = ForecastRepositoryImpl(dataSource, dispatcher)


    @Provides
    @Singleton
    fun provideLocationRepository(
        dataSource: LocationDataSource,
        dispatcher: CoroutineDispatcher
    ): LocationRepository = LocationRepositoryImpl(dataSource, dispatcher)

    @Provides
    fun providesCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO
}