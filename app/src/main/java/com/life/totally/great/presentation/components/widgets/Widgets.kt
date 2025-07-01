package com.life.totally.great.presentation.components.widgets

import android.Manifest
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.himanshoe.charty.line.LineChart
import com.himanshoe.charty.line.config.LineChartConfig
import com.himanshoe.charty.line.model.LineData
import com.life.totally.great.R
import com.life.totally.great.presentation.screens.models.WeatherUiModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged


@Composable
fun DebugRecompose(tag: String) {
    val count = remember { mutableIntStateOf(0) }
    count.intValue++
    Log.d("Recompose", "$tag recomposed ${count.intValue} times")
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionHandler(
    onGranted: () -> Unit,
    onDenied: () -> Unit
) {
    val permissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    // To prevent multiple calls
    val wasPermissionGranted = rememberSaveable { mutableStateOf(false) }
    var showDialog by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(permissionState.allPermissionsGranted) {
        if (permissionState.allPermissionsGranted && !wasPermissionGranted.value) {
            wasPermissionGranted.value = true
            onGranted()
        } else if (!permissionState.allPermissionsGranted) {
            onDenied()
        }
    }

    if (!permissionState.allPermissionsGranted) {
        if (permissionState.shouldShowRationale && showDialog) {
            LocationRationaleDialog(
                onDismissRequest = { showDialog = false },
                onConfirm = {
                    permissionState.launchMultiplePermissionRequest()
                    showDialog = false
                }
            )
        } else {
            LaunchedEffect(Unit) {
                permissionState.launchMultiplePermissionRequest()
            }
        }
    }
}


@OptIn(FlowPreview::class)
@Composable
fun SearchView(
    modifier: Modifier = Modifier,
    initialText: String = "",
    hint: String = stringResource(R.string.search_dots),
    debounceMillis: Long = 500L,
    onSearch: (String) -> Unit
) {
    var searchText by remember { mutableStateOf(initialText) }

    LaunchedEffect(searchText) {
        snapshotFlow { searchText }
            .debounce(debounceMillis)
            .distinctUntilChanged()
            .collectLatest { onSearch(it) }
    }

    OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        placeholder = { Text(hint) },
        singleLine = true,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun WeatherCard(
    model: WeatherUiModel,
    modifier: Modifier = Modifier
) {
    //remember values to avoid recomposition
    val timeString = remember(model.timeString) { "${model.timeString}" }
    val cityName = remember(model.cityName) { model.cityName }
    val description = remember(model.description) { model.description }
    val temp = remember(model.temp) { "Temperature: ${model.temp}°C" }
    val feelsLike = remember(model.feelsLike) { "Feels like: ${model.feelsLike}°C" }
    val tempMin = remember(model.tempMin) { "Min: ${model.tempMin}°C" }
    val tempMax = remember(model.tempMax) { "Max: ${model.tempMax}°C" }
    val humidity = remember(model.humidity) { "Humidity: ${model.humidity}%" }
    val pressure = remember(model.pressure) { "Pressure: ${model.pressure} hPa" }
    val seaLevel = remember(model.seaLevel) { "Sea Level: ${model.seaLevel} hPa" }
    val grndLevel = remember(model.grndLevel) { "Ground Level: ${model.grndLevel} hPa" }
    val windSpeed = remember(model.windSpeed) { "Wind Speed: ${model.windSpeed} m/s" }
    val rainOneHourVolume =
        remember(model.rainOneHourVolume) { "Rain (1h): ${model.rainOneHourVolume} mm" }

    // UI
    Card(
        modifier = modifier
            .width(dimensionResource(id = R.dimen.weather_card_width))// fixed width for performance, but we can check screen size,
            // and use percentage from screen size for setting up card size
            .height(dimensionResource(id = R.dimen.weather_card_height)),// fixed height for performance
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = modifier.padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberAsyncImagePainter(model.iconUrl),
                    contentDescription = model.description,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(timeString, style = MaterialTheme.typography.titleLarge)
                    Text(cityName, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = description,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(temp, style = MaterialTheme.typography.bodyLarge)
            Text(feelsLike, style = MaterialTheme.typography.bodyMedium)
            Text(
                tempMin + tempMax,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(humidity)
            Text(pressure)
            Text(seaLevel)
            Text(grndLevel)
            Text(windSpeed)
            Text(rainOneHourVolume)
        }
    }
}

@Composable
fun WeatherMiniCard(
    model: WeatherUiModel,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val painter = rememberAsyncImagePainter(model.iconUrl)
                Image(
                    painter = painter,
                    contentDescription = model.description,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = model.cityName, style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = "${model.main} - ${model.description}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (model.dateString != null && model.timeString != null) {
                        Text(
                            text = "${model.dateString} ${model.timeString}",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Temperature: ${model.temp}°C", style = MaterialTheme.typography.bodyLarge)
            Text("Feels like: ${model.feelsLike}°C", style = MaterialTheme.typography.bodyMedium)
            Text(
                "Min: ${model.tempMin}°C, Max: ${model.tempMax}°C",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
    DebugRecompose("WeatherMiniCard ${model.dateString}")
}

@Composable
fun ErrorMessageCard(
    message: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Composable
fun InfoMessageCard(
    message: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Composable
fun ForecastItemCard(
    date: String,
    model: WeatherUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val temp = remember(model.temp) { "${model.temp.toInt()}°C" }
    val description = remember(model.description) { "${model.description}  $temp" }
    Card(
        modifier = modifier
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = model.iconUrl,
                contentDescription = model.description,
                modifier = Modifier.size(48.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(date, style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1
            )
        }
    }
}

@Composable
fun TemperatureLineChart(
    weatherItems: List<LineData>,
    modifier: Modifier = Modifier
) {

    LineChart(
        data = { weatherItems },
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(8.dp),
        chartConfig = LineChartConfig(),
    )
}

