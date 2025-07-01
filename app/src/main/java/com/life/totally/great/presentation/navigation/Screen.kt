package com.life.totally.great.presentation.navigation

import android.net.Uri

const val ROUTE_WEATHER = "main"
const val ROUTE_FORECAST = "forecastDetail/{date}"

sealed class Screen(val route: String) {
    data object Main : Screen(ROUTE_WEATHER)

    data object ForecastDetail : Screen(ROUTE_FORECAST) {
        fun createRoute(date: String) =
            "forecastDetail/${Uri.encode(date)}"
    }
}