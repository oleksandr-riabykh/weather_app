package com.life.totally.great.presentation.screens.models

import androidx.compose.runtime.Immutable
import com.himanshoe.charty.line.model.LineData

@Immutable
data class ForecastUiModel(
    val date: String,
    val weatherForecastMap: List<WeatherUiModel>,
    val chartData: List<LineData>
)
