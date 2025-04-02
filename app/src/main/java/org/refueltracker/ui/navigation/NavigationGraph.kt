package org.refueltracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import org.refueltracker.ui.fuelstop.FuelStopEntryScreen
import org.refueltracker.ui.fuelstop.FuelStopEntryDestination
import org.refueltracker.ui.home.HomeDestination
import org.refueltracker.ui.home.HomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToFuelStopEntry = { navController.navigate(FuelStopEntryDestination.route) }
            )
        }
        composable(route = FuelStopEntryDestination.route) {
            FuelStopEntryScreen(
                onNavigateUp = navController::navigateUp,
                onSaveClickNavigateTo = { navController.popBackStack(HomeDestination.route, inclusive = false) }
            )
        }
    }
}