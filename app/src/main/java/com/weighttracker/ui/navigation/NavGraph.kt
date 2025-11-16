package com.weighttracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.weighttracker.ui.screens.*
import com.weighttracker.viewmodel.WeightViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Charts : Screen("charts")
    object Goals : Screen("goals")
    object Profile : Screen("profile")
    object AddWeight : Screen("add_weight")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: WeightViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToAddWeight = { navController.navigate(Screen.AddWeight.route) },
                onNavigateToCharts = { navController.navigate(Screen.Charts.route) }
            )
        }

        composable(Screen.Charts.route) {
            ChartsScreen(viewModel = viewModel)
        }

        composable(Screen.Goals.route) {
            GoalsScreen(viewModel = viewModel)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(viewModel = viewModel)
        }

        composable(Screen.AddWeight.route) {
            AddWeightScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
