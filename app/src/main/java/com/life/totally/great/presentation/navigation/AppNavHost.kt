package com.life.totally.great.presentation.navigation

import WeatherScreen
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.life.totally.great.presentation.screens.details.ForecastDetailScreen
import com.life.totally.great.presentation.screens.shared.MainViewModel

private const val ARG_DATE = "date"

@Composable
fun AppNavHost(
    innerPadding: PaddingValues,
    navController: NavHostController = rememberNavController()
) {
    val vm: MainViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Screen.Main.route) {
            WeatherScreen(vm, navController)
        }
        composable(
            route = Screen.ForecastDetail.route,
            arguments = listOf(
                navArgument(ARG_DATE) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getString(ARG_DATE) ?: ""
            ForecastDetailScreen(vm, date, navController)
        }
    }
}