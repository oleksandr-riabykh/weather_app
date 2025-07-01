package com.life.totally.great.presentation.screens.details

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.life.totally.great.R
import com.life.totally.great.presentation.components.buttons.VioletButton
import com.life.totally.great.presentation.components.containers.CoreColumnContainer
import com.life.totally.great.presentation.components.text.TitleLabel
import com.life.totally.great.presentation.components.widgets.ErrorMessageCard
import com.life.totally.great.presentation.components.widgets.ForecastHorizontalList
import com.life.totally.great.presentation.components.widgets.TemperatureLineChart
import com.life.totally.great.presentation.screens.models.ForecastUiModel
import com.life.totally.great.presentation.screens.shared.MainIntent
import com.life.totally.great.presentation.screens.shared.MainSideEffect
import com.life.totally.great.presentation.screens.shared.MainUiState
import com.life.totally.great.presentation.screens.shared.MainViewModel

@Composable
fun ForecastDetailScreen(
    viewModel: MainViewModel,
    date: String,
    nav: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.processIntent(MainIntent.GetSelectedForecast(date))
    }

    val detailsState by viewModel.detailsState.collectAsStateWithLifecycle()
    ObserveForecastSideEffects(viewModel, nav)

    when (val state = detailsState) {
        is MainUiState.Loading -> {
            CircularProgressIndicator()
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

    CoreColumnContainer(modifier = Modifier.verticalScroll(rememberScrollState())) {
        TitleLabel(text = forecast.date)
        Spacer(Modifier.height(16.dp))

        ForecastHorizontalList(weatherEntries = weatherEntries)
        Spacer(modifier = Modifier.width(16.dp))

        TemperatureLineChart(forecast.chartData)
        Spacer(modifier = Modifier.width(16.dp))
        VioletButton(
            labelId = R.string.close,
            actionDelegate = onCloseClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
    }


}

@Composable
fun ObserveForecastSideEffects(viewModel: MainViewModel, nav: NavController) {

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MainSideEffect.CloseDetails -> {
                    nav.navigateUp()
                }

                else -> Unit
            }
        }
    }
}