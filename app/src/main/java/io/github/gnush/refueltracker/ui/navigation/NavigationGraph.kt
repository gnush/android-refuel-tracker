package io.github.gnush.refueltracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.github.gnush.refueltracker.ui.settings.SettingsDestination
import io.github.gnush.refueltracker.ui.settings.SettingsScreen
import io.github.gnush.refueltracker.ui.fuelstop.FuelStopCalendarDestination
import io.github.gnush.refueltracker.ui.fuelstop.FuelStopCalendarScreen
import io.github.gnush.refueltracker.ui.fuelstop.FuelStopEditDestination
import io.github.gnush.refueltracker.ui.fuelstop.FuelStopEditScreen
import io.github.gnush.refueltracker.ui.fuelstop.FuelStopEntryScreen
import io.github.gnush.refueltracker.ui.fuelstop.FuelStopEntryDestination
import io.github.gnush.refueltracker.ui.fuelstop.FuelStopListDestination
import io.github.gnush.refueltracker.ui.fuelstop.FuelStopListScreen
import io.github.gnush.refueltracker.ui.settings.AboutDestination
import io.github.gnush.refueltracker.ui.settings.AboutScreen
import io.github.gnush.refueltracker.ui.settings.LibrariesDestination
import io.github.gnush.refueltracker.ui.settings.LibrariesScreen
import io.github.gnush.refueltracker.ui.statistic.StatisticsHomeDestination
import io.github.gnush.refueltracker.ui.statistic.StatisticsHomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = FuelStopListDestination.route,
        modifier = modifier
    ) {
        composable(route = FuelStopListDestination.route) {
            FuelStopListScreen(
                navigateTo = navController::navigate,
                navigateToSettings = { navController.navigate(SettingsDestination.route) },
                navigateToAbout = { navController.navigate(AboutDestination.route) },
                navigateToFuelStopEntry = { navController.navigate(FuelStopEntryDestination.route) },
                navigateToFuelStopEdit = { navController.navigate(FuelStopEditDestination.routeWithFuelStopId(it)) },
            )
        }
        composable(route = FuelStopCalendarDestination.route) {
            FuelStopCalendarScreen(
                navigateTo = navController::navigate,
                navigateToSettings = { navController.navigate(SettingsDestination.route) },
                navigateToAbout = { navController.navigate(AboutDestination.route) },
                navigateToFuelStopEntry = { navController.navigate(FuelStopEntryDestination.route) },
                navigateToFuelStopEdit = { navController.navigate(FuelStopEditDestination.routeWithFuelStopId(it)) },
            )
        }
        composable(route = FuelStopEntryDestination.route) {
            FuelStopEntryScreen(
                onNavigateUp = navController::navigateUp,
                navigateToSettings = { navController.navigate(SettingsDestination.route) },
                onSaveClickNavigateTo = navController::popBackStack
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
                onSaveClickNavigateTo = navController::popBackStack
            )
        }
        composable(StatisticsHomeDestination.route) {
            StatisticsHomeScreen(
                navigateTo = navController::navigate,
                navigateToSettings = { navController.navigate(SettingsDestination.route) },
                navigateToAbout = { navController.navigate(AboutDestination.route) },
            )
        }
        composable(SettingsDestination.route) {
            SettingsScreen(
                onNavigateUp = navController::navigateUp
            )
        }
        composable(AboutDestination.route) {
            AboutScreen(
                onNavigateUp = navController::navigateUp,
                navigateToLibraries = { navController.navigate(LibrariesDestination.route) }
            )
        }
        composable(LibrariesDestination.route) {
            LibrariesScreen(
                onNavigateUp = navController::navigateUp
            )
        }
    }
}