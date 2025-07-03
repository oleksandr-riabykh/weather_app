package com.life.totally.great.presentation.screens.details

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.presentation.MainActivity
import com.life.totally.great.presentation.Tags.CLOSE_BUTTON
import com.life.totally.great.presentation.Tags.LOADING_INDICATOR
import com.life.totally.great.presentation.screens.base.BaseTestClass
import com.life.totally.great.presentation.screens.models.ForecastUiModel
import com.life.totally.great.presentation.screens.models.toWeatherUiModel
import com.life.totally.great.presentation.screens.shared.MainIntent
import com.life.totally.great.presentation.screens.shared.MainUiState
import com.life.totally.great.presentation.screens.shared.MainViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ForecastDetailScreenTest: BaseTestClass() {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: MainViewModel
    private lateinit var navController: NavController

    private val loadingState = MainUiState.Loading
    private val errorState = MainUiState.Error(WeatherError.NoLocation("Test error"))
    private val successState = MainUiState.Success(
        ForecastUiModel(
            date = "2025-07-01",
            weatherForecastMap = mockTestForecastResponse.forecastList.map{it.toWeatherUiModel("test name")},
            chartData = listOf(/* ... */)
        )
    )

    private val detailsStateFlow = MutableStateFlow<MainUiState<ForecastUiModel>>(loadingState)
    private val detailsEffectFlow = MutableSharedFlow<DetailsSideEffect>(1)

    @Before
    fun setup() {
        viewModel = mockk<MainViewModel>(relaxed = true) {
            every { detailsState } returns detailsStateFlow
        }
        every { viewModel.detailsState } returns detailsStateFlow
        every { viewModel.detailsEffect } returns detailsEffectFlow.asSharedFlow()
        navController = mockk(relaxed = true)
    }

    @Test
    fun showsSearchView_whenStateIsLoading() {
        detailsStateFlow.value = loadingState
        composeTestRule.setContent {
            ForecastDetailScreen(viewModel, "2025-07-01", navController)
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(LOADING_INDICATOR).assertIsDisplayed()
    }

    @Test
    fun showsErrorMessage_whenStateIsError() {
        detailsStateFlow.value = errorState
        composeTestRule.setContent {
            ForecastDetailScreen(viewModel, "2025-07-01", navController)
        }
        composeTestRule.onNodeWithText("Test error").assertIsDisplayed()
    }

    @Test
    fun showsForecastDetails_whenStateIsSuccess() {
        detailsStateFlow.value = successState
        composeTestRule.setContent {
            ForecastDetailScreen(viewModel, "2025-07-01", navController)
        }
        composeTestRule.onNodeWithText("2025-07-01").assertIsDisplayed()
        // Add more assertions for your forecast content if needed
    }

    @Test
    fun closeButtonTriggersIntent() {
        detailsStateFlow.value = successState
        composeTestRule.setContent {
            ForecastDetailScreen(viewModel, "2025-07-01", navController)
        }
        composeTestRule.onNodeWithTag(CLOSE_BUTTON).performClick()
        verify { viewModel.processIntent(MainIntent.CloseDetails) }
    }
}