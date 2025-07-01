package com.life.totally.great.presentation.screens.models

import androidx.compose.runtime.Immutable

@Immutable
data class MainUIDataModel(
    val weather: WeatherUiModel,
    val forecastList: List<ForecastUiModel>,
)
