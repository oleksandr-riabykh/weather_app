package com.life.totally.great.presentation.screens.models

import com.himanshoe.charty.line.model.LineData

data class ForecastUiModel(
    val date: String,
    val weatherForecastMap: List<WeatherUiModel>,
    val chartData: List<LineData>
)
