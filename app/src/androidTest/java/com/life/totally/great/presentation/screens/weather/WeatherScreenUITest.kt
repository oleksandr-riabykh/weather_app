package com.life.totally.great.presentation.screens.weather

import WeatherScreen
import android.content.Context
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.life.totally.great.data.models.Coordinates
import com.life.totally.great.data.models.GeoLocation
import com.life.totally.great.presentation.screens.base.BaseTestClass
import com.life.totally.great.presentation.screens.models.MainUIDataModel
import com.life.totally.great.presentation.screens.shared.MainUiState
import com.life.totally.great.presentation.screens.shared.MainViewModel
import com.life.totally.great.utils.factories.GeoLocationFactory
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
@LargeTest
class WeatherScreenUITest: BaseTestClass() {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Mock
    private lateinit var mockMainViewModel: MainViewModel

    @Mock
    private lateinit var mockNavController: NavController

    @Mock
    private lateinit var mockContext: Context

    private lateinit var searchStateFlow: MutableStateFlow<MainUiState<List<GeoLocation>>>
    private lateinit var weatherStateFlow: MutableStateFlow<MainUiState<MainUIDataModel>>
    private lateinit var effectFlow: MutableSharedFlow<WeatherSideEffect>

    private val mockTestCityName = "SearchTest"
    private val mockResponseCityName = "testonto"
    private val mockErrorMessage = "Network error"
    private val mockCoordinates = Coordinates(-74.0, 40.0)
    private val mockCity =
        GeoLocationFactory.create(
            name = mockTestCityName,
            lat = mockCoordinates.lat,
            lon = mockCoordinates.lon
        )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        // Initialize StateFlows
        searchStateFlow = MutableStateFlow(MainUiState.Idle)
        weatherStateFlow = MutableStateFlow(MainUiState.Idle)
        effectFlow = MutableSharedFlow()

        // Mock ViewModel StateFlows
        `when`(mockMainViewModel.searchState).thenReturn(searchStateFlow.asStateFlow())
        `when`(mockMainViewModel.weatherState).thenReturn(weatherStateFlow.asStateFlow())
        `when`(mockMainViewModel.effect).thenReturn(effectFlow.asSharedFlow())
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun weatherScreen_initialState_displaysSearchView() {
        composeTestRule.setContent {
            WeatherScreen(
                mainViewModel = mockMainViewModel,
                nav = mockNavController
            )
        }

        // Solution 1: Use waitUntil with timeout
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule
                .onAllNodesWithTag("search_view")
                .fetchSemanticsNodes().size == 1
        }

        // Verify SearchView is displayed
        composeTestRule.onNodeWithTag("search_view").assertIsDisplayed()

        // Solution 2: Use waitUntilExactlyOneExists
        composeTestRule.waitUntilExactlyOneExists(
            matcher = hasTestTag("search_results_list"),
            timeoutMillis = 5000
        )
        // Solution 3: Wait for compose to be idle
        composeTestRule.waitForIdle()

        // Verify empty search results list is displayed
        composeTestRule.onNodeWithTag("search_results_list").assertIsDisplayed()
    }
}