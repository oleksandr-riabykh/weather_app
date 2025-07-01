import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.life.totally.great.R
import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.presentation.components.containers.CoreColumnContainer
import com.life.totally.great.presentation.components.widgets.DebugRecompose
import com.life.totally.great.presentation.components.widgets.ErrorMessageCard
import com.life.totally.great.presentation.components.widgets.ForecastMainList
import com.life.totally.great.presentation.components.widgets.InfoMessageCard
import com.life.totally.great.presentation.components.widgets.LocationPermissionHandler
import com.life.totally.great.presentation.components.widgets.SearchResultsList
import com.life.totally.great.presentation.components.widgets.SearchView
import com.life.totally.great.presentation.components.widgets.WeatherMiniCard
import com.life.totally.great.presentation.navigation.Screen
import com.life.totally.great.presentation.screens.shared.MainIntent
import com.life.totally.great.presentation.screens.shared.MainUiState
import com.life.totally.great.presentation.screens.shared.MainViewModel
import com.life.totally.great.presentation.screens.weather.WeatherSideEffect

@Composable
fun WeatherScreen(
    mainViewModel: MainViewModel,
    nav: NavController
) {

    // init
    val searchState by mainViewModel.searchState.collectAsStateWithLifecycle()
    val weatherState by mainViewModel.weatherState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    LocationPermissionHandler(onGranted = {
        mainViewModel.processIntent(MainIntent.LocationGranted)
    }, onDenied = {
        mainViewModel.processIntent(MainIntent.LocationDenied)
    })

    // Effects
    LaunchedEffect(Unit) {
        mainViewModel.effect.collect { effect ->
            when (effect) {
                is WeatherSideEffect.NavigateToDetails -> {
                    nav.navigate(Screen.ForecastDetail.createRoute(effect.date))
                }

                is WeatherSideEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // UI composable components
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
                        mainViewModel.processIntent(
                            MainIntent.LoadWeatherByCoordinates(
                                it.lat,
                                it.lon
                            )
                        )
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
        when (val state = weatherState) {
            MainUiState.Loading -> CircularProgressIndicator()
            is MainUiState.Success -> {
                val data = state.data
                WeatherMiniCard(data.weather)
                Spacer(Modifier.height(16.dp))
                ForecastMainList(data.forecastList, onDateClick = { item ->
                    mainViewModel.processIntent(MainIntent.ForecastItemClicked(item))
                })
            }

            is MainUiState.Error -> {
                if (state.error is WeatherError.NoLocation) {
                    InfoMessageCard(
                        stringResource(
                            R.string.location_empty_message,
                            state.error.message
                                ?: stringResource(R.string.unknown_error)
                        )
                    )
                } else {
                    ErrorMessageCard(
                        stringResource(
                            R.string.weather_error,
                            state.error.message
                                ?: stringResource(R.string.unknown_error)
                        )
                    )

                }
            }

            else -> Unit
        }
    }
    DebugRecompose("WeatherScreen")
}