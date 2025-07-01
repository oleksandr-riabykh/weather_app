package com.life.totally.great.presentation.screens.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.data.models.GeoLocation
import com.life.totally.great.domain.usecases.forecast.LoadForecastByCityUseCase
import com.life.totally.great.domain.usecases.forecast.LoadForecastCurrentLocationUseCase
import com.life.totally.great.domain.usecases.search.SearchCityUseCase
import com.life.totally.great.domain.usecases.weather.LoadWeatherByCityUseCase
import com.life.totally.great.domain.usecases.weather.LoadWeatherByCoordinatesUseCase
import com.life.totally.great.domain.usecases.weather.LoadWeatherCurrentLocationUseCase
import com.life.totally.great.presentation.screens.models.ForecastUiModel
import com.life.totally.great.presentation.screens.models.WeatherUiModel
import com.life.totally.great.presentation.screens.models.toUiModel
import com.life.totally.great.presentation.screens.models.toUiModelList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchCityUseCase: SearchCityUseCase,
    private val loadForecastUseCase: LoadForecastByCityUseCase,
    private val loadWeatherByCityUseCase: LoadWeatherByCityUseCase,
    private val loadWeatherByCoordsUseCase: LoadWeatherByCoordinatesUseCase,
    private val loadWeatherLocationUseCase: LoadWeatherCurrentLocationUseCase,
    private val loadForecastLocationUseCase: LoadForecastCurrentLocationUseCase
) : ViewModel() {

    private val _searchState = MutableStateFlow<MainUiState<List<GeoLocation>>>(MainUiState.Idle)
    val searchState: StateFlow<MainUiState<List<GeoLocation>>> = _searchState.asStateFlow()

    private val _forecastState =
        MutableStateFlow<MainUiState<List<ForecastUiModel>>>(MainUiState.Idle)
    val forecastState: StateFlow<MainUiState<List<ForecastUiModel>>> = _forecastState.asStateFlow()

    private val _weatherState = MutableStateFlow<MainUiState<WeatherUiModel>>(MainUiState.Idle)
    val weatherState: StateFlow<MainUiState<WeatherUiModel>> = _weatherState.asStateFlow()

    private val _detailsState = MutableStateFlow<MainUiState<ForecastUiModel>>(MainUiState.Idle)
    val detailsState: StateFlow<MainUiState<ForecastUiModel>> = _detailsState.asStateFlow()

    private val _effect = MutableSharedFlow<MainSideEffect>()
    val effect = _effect.asSharedFlow()

    fun processIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.SearchCity -> searchCity(intent.name)
            is MainIntent.LoadForecastByCity -> loadForecastForCity(intent.city)
            is MainIntent.LoadWeatherByCity -> loadWeatherForCity(intent.city)
            is MainIntent.LoadWeatherByCoordinates -> loadWeatherForCoords(intent.lat, intent.lon)
            is MainIntent.ForecastItemClicked -> clickForecastItem(intent.date)
            is MainIntent.RequestLocation -> requestLocation()
            is MainIntent.LocationDenied -> showLocationDenied()
            is MainIntent.LocationGranted -> {
                loadCurrentWeatherLocationData()
                loadCurrentForecastLocationData()
            }

            is MainIntent.CloseDetails -> closeDetails()
            is MainIntent.GetSelectedForecast -> selectForecast(intent.date)
        }
    }

    private fun closeDetails() {
        viewModelScope.launch {
            _effect.emit(MainSideEffect.CloseDetails)
        }
    }

    private fun clickForecastItem(date: String) {
        viewModelScope.launch {
            _effect.emit(MainSideEffect.NavigateToDetails(date))
        }
    }

    private fun showLocationDenied() {
        viewModelScope.launch {
            _effect.emit(MainSideEffect.RequestLocationPermission)
        }
    }

    private fun requestLocation() {
        viewModelScope.launch {
            _effect.emit(MainSideEffect.RequestLocationPermission)
        }
    }

    private fun selectForecast(date: String) {
        viewModelScope.launch {
            val data = getForecastByDate(date)
            _detailsState.value =
                if (data != null) MainUiState.Success(data) else MainUiState.Error(
                    WeatherError.NotFound("Can't find forecast")
                )
        }
    }

    private fun loadCurrentWeatherLocationData() {
        viewModelScope.launch {
            _searchState.value = MainUiState.Idle
            loadWeatherLocationUseCase()
                .collect { result ->
                    _weatherState.value = when (result) {
                        is DataResult.Success -> MainUiState.Success(result.data.toUiModel())
                        is DataResult.Error -> MainUiState.Error(result.error)
                    }
                }
        }
    }

    private fun loadCurrentForecastLocationData() {
        viewModelScope.launch {
            loadForecastLocationUseCase()
                .collect { result ->
                    _forecastState.value = when (result) {
                        is DataResult.Success -> {
                            val data = result.data.toUiModelList()
                            MainUiState.Success(data)
                        }

                        is DataResult.Error -> MainUiState.Error(result.error)
                    }
                }
        }
    }

    private fun searchCity(name: String) {
        if (name.isBlank()) {
            loadCurrentWeatherLocationData()
            loadCurrentForecastLocationData()
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

    private fun loadForecastForCity(city: GeoLocation) {
        viewModelScope.launch {
            loadForecastUseCase(city.name)
                .onStart { _forecastState.value = MainUiState.Loading }
                .collect { result ->
                    _forecastState.value = when (result) {
                        is DataResult.Success -> MainUiState.Success(result.data.toUiModelList())
                        is DataResult.Error -> MainUiState.Error(result.error)
                    }
                }
        }
    }

    private fun loadWeatherForCity(city: GeoLocation) {
        viewModelScope.launch {
            loadWeatherByCityUseCase(city.name)
                .onStart { _weatherState.value = MainUiState.Loading }
                .collect { result ->
                    _weatherState.value = when (result) {
                        is DataResult.Success -> MainUiState.Success(result.data.toUiModel())
                        is DataResult.Error -> MainUiState.Error(result.error)
                    }
                }
        }
    }

    private fun loadWeatherForCoords(lat: Double, lon: Double) {
        viewModelScope.launch {
            loadWeatherByCoordsUseCase(lat, lon)
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
        return (forecastState.value as? MainUiState.Success)
            ?.data
            ?.firstOrNull { it.date == date }
    }
}