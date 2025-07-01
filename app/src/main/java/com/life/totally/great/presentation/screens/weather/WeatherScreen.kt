
import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.life.totally.great.R
import com.life.totally.great.presentation.components.containers.CoreColumnContainer
import com.life.totally.great.presentation.components.widgets.ErrorMessageCard
import com.life.totally.great.presentation.components.widgets.ForecastMainList
import com.life.totally.great.presentation.components.widgets.LocationPermissionChecker
import com.life.totally.great.presentation.components.widgets.LocationPermissionDialog
import com.life.totally.great.presentation.components.widgets.LocationRationaleDialog
import com.life.totally.great.presentation.components.widgets.SearchResultsList
import com.life.totally.great.presentation.components.widgets.SearchView
import com.life.totally.great.presentation.components.widgets.WeatherMiniCard
import com.life.totally.great.presentation.navigation.Screen
import com.life.totally.great.presentation.screens.shared.MainIntent
import com.life.totally.great.presentation.screens.shared.MainSideEffect
import com.life.totally.great.presentation.screens.shared.MainUiState
import com.life.totally.great.presentation.screens.shared.MainViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherScreen(
    mainViewModel: MainViewModel,
    nav: NavController
) {

    // init
    /**
     * we could consider search, weather, forecast as stateful components in the future.
     * Also, there will be viewmodels for each components.
     */
    val searchState by mainViewModel.searchState.collectAsStateWithLifecycle()
    val weatherState by mainViewModel.weatherState.collectAsStateWithLifecycle()
    val forecastState by mainViewModel.forecastState.collectAsStateWithLifecycle()

    var showLocationDialog by remember { mutableStateOf(false) }
    var showRationaleDialog by remember { mutableStateOf(false) }
    val multiplePermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    val context = LocalContext.current
    LocationPermissionChecker(mainViewModel, multiplePermissionsState)

    // Effects
    LaunchedEffect(Unit) {
        mainViewModel.effect.collect { effect ->
            when (effect) {
                is MainSideEffect.NavigateToDetails -> {
                    nav.navigate(Screen.ForecastDetail.createRoute(effect.date))
                }

                is MainSideEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()
                }

                is MainSideEffect.RequestLocationPermission -> {
                    showLocationDialog = true
                }

                else -> Unit
            }
        }
    }
    LaunchedEffect(multiplePermissionsState) {
        if (multiplePermissionsState.allPermissionsGranted) {
            mainViewModel.processIntent(MainIntent.LocationGranted)
            showLocationDialog = false
        } else {
            mainViewModel.processIntent(MainIntent.LocationDenied)
        }
    }

    // UI composable components
    if (showLocationDialog) {
        LocationPermissionDialog(
            onGrantClick = {
                if (multiplePermissionsState.allPermissionsGranted) {
                    // Permission granted, start location tracking
                    showLocationDialog = false
                    showRationaleDialog = false
                } else if (multiplePermissionsState.shouldShowRationale) {
                    showLocationDialog = true
                    showRationaleDialog = true

                } else {
                    multiplePermissionsState.launchMultiplePermissionRequest()
                }
            },
            onDismissClick = {
                showRationaleDialog = true
            }
        )
    }
    if (showRationaleDialog) {
        LocationRationaleDialog(onDismissRequest = {
            showRationaleDialog = true
        }, onConfirm = {
            multiplePermissionsState.launchMultiplePermissionRequest()
            showLocationDialog = false
            showRationaleDialog = false
        })
    }
    CoreColumnContainer {
        SearchView(
            onSearch = { city ->
                mainViewModel.processIntent(MainIntent.SearchCity(city))
            }
        )

        Spacer(Modifier.height(8.dp))
        when (val state = searchState) {
            MainUiState.Loading -> CircularProgressIndicator()
            is MainUiState.Success -> {
                val cities = state.data
                SearchResultsList(
                    cities = cities,
                    onCityClick = {
                        mainViewModel.processIntent(MainIntent.LoadWeatherByCity(it))
                        mainViewModel.processIntent(MainIntent.LoadForecastByCity(it))
                    },
                )
            }

            is MainUiState.Error -> {
                val error = state.error
                ErrorMessageCard(
                    stringResource(
                        R.string.city_error,
                        error.message ?: stringResource(R.string.unknown_error)
                    )
                )
            }

            is MainUiState.Idle -> {
                SearchResultsList(
                    cities = listOf()
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        when (weatherState) {
            MainUiState.Loading -> CircularProgressIndicator()
            is MainUiState.Success -> {
                val weather = (weatherState as MainUiState.Success).data
                WeatherMiniCard(weather)
            }

            is MainUiState.Error -> ErrorMessageCard(
                stringResource(
                    R.string.weather_error,
                    (weatherState as MainUiState.Error).error.message
                        ?: stringResource(R.string.unknown_error)
                )
            )

            else -> Unit
        }

        Spacer(Modifier.height(16.dp))
        when (forecastState) {
            MainUiState.Loading -> CircularProgressIndicator()
            is MainUiState.Success -> {
                val forecast = (forecastState as MainUiState.Success).data
                ForecastMainList(forecast, onDateClick = { item ->
                    mainViewModel.processIntent(MainIntent.ForecastItemClicked(item))
                })
            }

            is MainUiState.Error -> ErrorMessageCard(
                stringResource(
                    R.string.forecast_error,
                    (forecastState as MainUiState.Error).error.message
                        ?: stringResource(R.string.unknown_error)
                )
            )

            else -> Unit
        }
    }
}