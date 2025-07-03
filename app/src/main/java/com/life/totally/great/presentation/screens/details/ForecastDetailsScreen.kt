package com.life.totally.great.presentation.screens.details

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.life.totally.great.presentation.Tags.CLOSE_BUTTON
import com.life.totally.great.presentation.Tags.FORECAST_DETAIL_SCREEN
import com.life.totally.great.presentation.Tags.FORECAST_LIST
import com.life.totally.great.presentation.Tags.LOADING_INDICATOR
import com.life.totally.great.presentation.components.buttons.CloseButton
import com.life.totally.great.presentation.components.containers.CoreColumnContainer
import com.life.totally.great.presentation.components.text.TitleLabel
import com.life.totally.great.presentation.components.widgets.DebugRecompose
import com.life.totally.great.presentation.components.widgets.ErrorMessageCard
import com.life.totally.great.presentation.components.widgets.ForecastHorizontalList
import com.life.totally.great.presentation.components.widgets.TemperatureLineChart
import com.life.totally.great.presentation.screens.models.ForecastUiModel
import com.life.totally.great.presentation.screens.shared.MainIntent
import com.life.totally.great.presentation.screens.shared.MainUiState
import com.life.totally.great.presentation.screens.shared.MainViewModel

@Composable
fun ForecastDetailScreen(
    viewModel: MainViewModel,
    date: String,
    nav: NavController
) {
    LaunchedEffect(date) {
        viewModel.processIntent(MainIntent.GetSelectedForecast(date))
    }
    ObserveForecastSideEffects(viewModel, nav)

    val detailsState by viewModel.detailsState.collectAsStateWithLifecycle()

    when (val state = detailsState) {
        is MainUiState.Loading -> {
            CircularProgressIndicator(modifier = Modifier.testTag(LOADING_INDICATOR))
        }

        is MainUiState.Success -> {
            ForecastDetailContent(
                forecast = state.data,
                onCloseClick = { viewModel.processIntent(MainIntent.CloseDetails) })
        }

        is MainUiState.Error -> {
            ErrorMessageCard(message = state.error.message ?: "Unknown error")
        }

        else -> Unit
    }
}

@Composable
fun ForecastDetailContent(forecast: ForecastUiModel, onCloseClick: () -> Unit) {

    val weatherEntries = forecast.weatherForecastMap
    val chartData by remember(forecast) {
        mutableStateOf(forecast.chartData)
    }

    CoreColumnContainer(modifier = Modifier.testTag(FORECAST_DETAIL_SCREEN)) {
        Row {
            TitleLabel(text = forecast.date)
            Spacer(Modifier.weight(1f))
            CloseButton(onCloseClick, modifier = Modifier.testTag(CLOSE_BUTTON))
        }

        Spacer(Modifier.height(24.dp))
        ForecastHorizontalList(
            weatherEntries = weatherEntries,
            modifier = Modifier.testTag(FORECAST_LIST)
        )
        Spacer(modifier = Modifier.weight(1f))
        TemperatureLineChart(chartData)
    }

    DebugRecompose("ForecastDetailContent")
}

@Composable
fun ObserveForecastSideEffects(viewModel: MainViewModel, nav: NavController) {

    LaunchedEffect(Unit) {
        viewModel.detailsEffect.collect { effect ->
            when (effect) {
                is DetailsSideEffect.CloseDetails -> {
                    nav.navigateUp()
                }
            }
        }
    }
}