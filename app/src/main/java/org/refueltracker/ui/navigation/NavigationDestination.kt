package org.refueltracker.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import org.refueltracker.ui.fuelstop.FuelStopHomeDestination
import org.refueltracker.ui.statistic.StatisticsHomeDestination

interface NavigationDestination {
    val route: String

    @get:StringRes
    val titleRes: Int
}

interface BottomNavigationDestination: NavigationDestination {
    val icon: ImageVector

    @get:StringRes
    val iconDescriptionRes: Int

    @get:StringRes
    val labelRes: Int
}

fun bottomNavBarDestinations(): List<BottomNavigationDestination> = listOf(
    FuelStopHomeDestination,
    StatisticsHomeDestination
)