package org.refueltracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.refueltracker.ui.fuelstop.FuelStopEditDestination
import org.refueltracker.ui.fuelstop.FuelStopEditScreen
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
                navigateTo = navController::navigate,
                navigateToFuelStopEntry = { navController.navigate(FuelStopEntryDestination.route) },
                navigateToFuelStopEdit = { navController.navigate(FuelStopEditDestination.routeWithFuelStopId(it)) },
            )
        }
        composable(route = FuelStopEntryDestination.route) {
            FuelStopEntryScreen(
                onNavigateUp = navController::navigateUp,
                onSaveClickNavigateTo = { navController.popBackStack(FuelStopHomeDestination.route, inclusive = false) }
            )
        }
        composable(
            route = FuelStopEditDestination.routeWithFuelStopId,
            arguments = listOf(navArgument(FuelStopEditDestination.FUEL_STOP_ID) {
                type = NavType.IntType
            })
        ) {
            FuelStopEditScreen(
                onNavigateUp = navController::navigateUp,
                onSaveClickNavigateTo = { navController.popBackStack(FuelStopHomeDestination.route, inclusive = false) }
            )
        }
    }
}