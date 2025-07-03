package com.life.totally.great.presentation.screens.weather

import WeatherScreen
import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.models.Coordinates
import com.life.totally.great.data.models.GeoLocation
import com.life.totally.great.presentation.Tags.LOADING_INDICATOR
import com.life.totally.great.presentation.screens.base.BaseTestClass
import com.life.totally.great.presentation.screens.models.MainUIDataModel
import com.life.totally.great.presentation.screens.models.toUiModel
import com.life.totally.great.presentation.screens.models.toUiModelList
import com.life.totally.great.presentation.screens.shared.MainUiState
import com.life.totally.great.presentation.screens.shared.MainViewModel
import com.life.totally.great.utils.factories.ForecastResponseFactory
import com.life.totally.great.utils.factories.GeoLocationFactory
import com.life.totally.great.utils.factories.WeatherResponseFactory
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
@LargeTest
class WeatherScreenUITest : BaseTestClass() {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Mock
    private lateinit var mockMainViewModel: MainViewModel

    @Mock
    private lateinit var mockNavController: NavController

    @Mock
    private lateinit var mockContext: Context


    private val searchStateFlow =
        MutableStateFlow<MainUiState<List<GeoLocation>>>(MainUiState.Loading)
    private val weatherStateFlow =
        MutableStateFlow<MainUiState<MainUIDataModel>>(MainUiState.Loading)
    private val effectFlow = MutableSharedFlow<WeatherSideEffect>(1)

    private val weatherUiModel = WeatherResponseFactory.create().toUiModel()
    private val forecastUiModel = ForecastResponseFactory.create().toUiModelList()

    private val loadingState = MainUiState.Loading
    private val errorState = MainUiState.Error(WeatherError.NoLocation("Test error"))
    private val successState = MainUiState.Success(
        MainUIDataModel(
            weatherUiModel,
            forecastUiModel
        )
    )

    @Before
    fun setup() {
        mockNavController = mockk(relaxed = true)
        mockMainViewModel = mockk<MainViewModel>(relaxed = true) {
            every { searchState } returns searchStateFlow
            every { weatherState } returns weatherStateFlow
            every { effect } returns effectFlow
        }
        every { mockMainViewModel.searchState } returns searchStateFlow
        every { mockMainViewModel.weatherState } returns weatherStateFlow
        every { mockMainViewModel.effect } returns effectFlow
    }


    // todo check location permission dialog first.
    @Test
    fun weatherScreen_loadingState_displaysLoadingIndicator() {
        searchStateFlow.value = loadingState
        weatherStateFlow.value = loadingState
        composeTestRule.setContent {
            WeatherScreen(
                mainViewModel = mockMainViewModel,
                nav = mockNavController
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(LOADING_INDICATOR).assertIsDisplayed()
    }
}