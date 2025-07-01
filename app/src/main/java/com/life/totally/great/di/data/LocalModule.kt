package com.life.totally.great.di.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.life.totally.great.data.persistance.LocationStoreManager
import com.life.totally.great.data.persistance.LocationStoreManagerImpl
import com.life.totally.great.data.scheduler.LocationUpdateSchedulerImpl
import com.life.totally.great.domain.scheduler.LocationUpdateScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "weather_app_prefs")

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideLocationStoreManager(
        @ApplicationContext context: Context
    ): LocationStoreManager = LocationStoreManagerImpl(context.dataStore)


    @Provides
    @Singleton
    fun provideLocationUpdateScheduler(
        @ApplicationContext context: Context
    ): LocationUpdateScheduler = LocationUpdateSchedulerImpl(context)
}