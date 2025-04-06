package org.refueltracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import org.refueltracker.ui.fuelstop.FuelStopEntryScreen
import org.refueltracker.ui.fuelstop.FuelStopEntryDestination
import org.refueltracker.ui.fuelstop.FuelStopHomeDestination
import org.refueltracker.ui.fuelstop.FuelStopHomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = FuelStopHomeDestination.route,
        modifier = modifier
    ) {
        composable(route = FuelStopHomeDestination.route) {
            FuelStopHomeScreen(
                navigateToFuelStopEntry = { navController.navigate(FuelStopEntryDestination.route) },
                navigateToFuelStopUpdate = {}, // TODO: Add update screen and navigate
            )
        }
        composable(route = FuelStopEntryDestination.route) {
            FuelStopEntryScreen(
                onNavigateUp = navController::navigateUp,
                onSaveClickNavigateTo = { navController.popBackStack(FuelStopHomeDestination.route, inclusive = false) }
            )
        }
    }
}