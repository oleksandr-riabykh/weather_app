package com.life.totally.great.di.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.life.totally.great.BuildConfig
import com.life.totally.great.data.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor? {
        return if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        } else {
            null // Return null if no logging for release builds
        }
    }

    // we can add there auth interceptor
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor?
    ): OkHttpClient {
        val httpClientBuilder = OkHttpClient.Builder()

        loggingInterceptor?.let {
            httpClientBuilder.addInterceptor(it)
        }
        return httpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            // add custom configurations if need
            .create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.WEATHER_API_HOST)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}