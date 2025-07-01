package com.life.totally.great.presentation.screens.models

data class MainUIDataModel(
    val weather: WeatherUiModel,
    val forecastList: List<ForecastUiModel>,
)
