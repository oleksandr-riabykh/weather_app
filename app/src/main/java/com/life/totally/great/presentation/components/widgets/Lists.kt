package com.life.totally.great.presentation.components.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.life.totally.great.data.models.GeoLocation
import com.life.totally.great.presentation.components.text.GrayStringText
import com.life.totally.great.presentation.screens.models.ForecastUiModel
import com.life.totally.great.presentation.screens.models.WeatherUiModel

@Composable
fun ForecastHorizontalList(
    weatherEntries: List<WeatherUiModel>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        userScrollEnabled = true
    ) {
        items(weatherEntries, key = { it.datetime }) { model ->
            WeatherCard(model,
                modifier = Modifier
                    .padding(vertical = 4.dp))
        }
    }
}

@Composable
fun ForecastMainList(
    forecastItems: List<ForecastUiModel>,
    onDateClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        userScrollEnabled = true
    ) {
        items(forecastItems, key = { it.date }) { item ->
                item.weatherForecastMap.firstOrNull()?.let { forecastItem ->
                ForecastItemCard(
                    date = forecastItem.dateString?:"",
                    model = forecastItem,
                    onClick = { onDateClick(item.date) }
                )
            }

        }
    }
}

@Composable
fun SearchResultsList(
    cities: List<GeoLocation>,
    modifier: Modifier = Modifier,
    onCityClick: (GeoLocation) -> Unit = {}
) {
    LazyColumn(modifier = modifier) {
        items(cities, key = { it.lat }) { city ->
            GrayStringText(
                text = city.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCityClick(city) }
                    .padding(8.dp)
            )
        }
    }
}