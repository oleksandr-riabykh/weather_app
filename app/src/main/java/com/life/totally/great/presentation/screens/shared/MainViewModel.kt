package com.life.totally.great.presentation.screens.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.data.models.GeoLocation
import com.life.totally.great.domain.usecases.LoadCurrentLocationUseCase
import com.life.totally.great.domain.usecases.LoadWeatherUseCase
import com.life.totally.great.domain.usecases.SearchCityUseCase
import com.life.totally.great.presentation.screens.details.DetailsSideEffect
import com.life.totally.great.presentation.screens.models.ForecastUiModel
import com.life.totally.great.presentation.screens.models.MainUIDataModel
import com.life.totally.great.presentation.screens.models.toUiModel
import com.life.totally.great.presentation.screens.weather.WeatherSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchCityUseCase: SearchCityUseCase,
    private val loadWeatherUseCase: LoadWeatherUseCase,
    private val loadLocationUseCase: LoadCurrentLocationUseCase
) : ViewModel() {

    private val _searchState = MutableStateFlow<MainUiState<List<GeoLocation>>>(MainUiState.Idle)
    val searchState: StateFlow<MainUiState<List<GeoLocation>>> = _searchState.asStateFlow()

    private val _weatherState = MutableStateFlow<MainUiState<MainUIDataModel>>(MainUiState.Idle)
    val weatherState: StateFlow<MainUiState<MainUIDataModel>> = _weatherState.asStateFlow()

    private val _detailsState = MutableStateFlow<MainUiState<ForecastUiModel>>(MainUiState.Idle)
    val detailsState: StateFlow<MainUiState<ForecastUiModel>> = _detailsState.asStateFlow()

    private val _effect = MutableSharedFlow<WeatherSideEffect>()
    val effect = _effect.asSharedFlow()

    private val _detailsEffect = MutableSharedFlow<DetailsSideEffect>()
    val detailsEffect = _detailsEffect.asSharedFlow()

    fun processIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.SearchCity -> searchCity(intent.name)
            is MainIntent.LoadWeatherByCoordinates -> loadWeatherForCoords(intent.lat, intent.lon)
            is MainIntent.ForecastItemClicked -> clickForecastItem(intent.date)
            is MainIntent.RequestLocation -> requestLocation()
            is MainIntent.LocationDenied -> showLocationDenied()
            is MainIntent.LocationGranted -> observeLocationChanges()
            is MainIntent.CloseDetails -> closeDetails()
            is MainIntent.GetSelectedForecast -> selectForecast(intent.date)
        }
    }

    private fun closeDetails() {
        viewModelScope.launch {
            _detailsEffect.emit(DetailsSideEffect.CloseDetails)
        }
    }

    private fun clickForecastItem(date: String) {
        viewModelScope.launch {
            _effect.emit(WeatherSideEffect.NavigateToDetails(date))
        }
    }

    private fun showLocationDenied() {
        viewModelScope.launch {
            _effect.emit(WeatherSideEffect.ShowError("Location permission denied"))
        }
    }

    private fun requestLocation() {
        viewModelScope.launch {
            _effect.emit(WeatherSideEffect.RequestLocationPermission)
        }
    }

    private fun selectForecast(date: String) {
        viewModelScope.launch {
            val data = getForecastByDate(date)
            _detailsState.value =
                if (data != null) MainUiState.Success(data) else MainUiState.Error(
                    WeatherError.NotFound("Can't find forecast") // should be localized
                )
        }
    }

    private fun observeLocationChanges() {
        viewModelScope.launch {
            loadLocationUseCase()
                .onEach { result ->
                    when (result) {
                        is DataResult.Success -> {
                            processIntent(
                                MainIntent.LoadWeatherByCoordinates(
                                    result.data.lat,
                                    result.data.lon
                                )
                            )
                        }

                        is DataResult.Error -> _weatherState.value = MainUiState.Error(result.error)

                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun searchCity(name: String) {
        if (name.isEmpty()) {
            _searchState.value = MainUiState.Success(emptyList())
            return
        }
        viewModelScope.launch {
            searchCityUseCase(name)
                .onStart { _searchState.value = MainUiState.Loading }
                .collect { result ->
                    _searchState.value = when (result) {
                        is DataResult.Success -> MainUiState.Success(result.data)
                        is DataResult.Error -> MainUiState.Error(result.error)
                    }
                }
        }
    }

    private fun loadWeatherForCoords(lat: Double, lon: Double) {
        viewModelScope.launch {
            loadWeatherUseCase(lat, lon)
                .onStart { _weatherState.value = MainUiState.Loading }
                .collect { result ->
                    _weatherState.value = when (result) {
                        is DataResult.Success -> MainUiState.Success(result.data.toUiModel())
                        is DataResult.Error -> MainUiState.Error(result.error)
                    }
                }
        }
    }

    private fun getForecastByDate(date: String): ForecastUiModel? {
        return (weatherState.value as? MainUiState.Success)
            ?.data?.forecastList
            ?.firstOrNull { it.date == date }
    }
}