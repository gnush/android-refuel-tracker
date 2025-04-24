package io.github.gnush.refueltracker

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import io.github.gnush.refueltracker.ui.fuelstop.FuelStopCalendarDestination
import io.github.gnush.refueltracker.ui.fuelstop.FuelStopEntryDestination
import io.github.gnush.refueltracker.ui.fuelstop.FuelStopListDestination
import io.github.gnush.refueltracker.ui.settings.SettingsDestination
import io.github.gnush.refueltracker.ui.statistic.StatisticsHomeDestination
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RefuelTrackerScreenNavigationTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setupRefuelTrackerNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            RefuelTrackerApp(navController)
        }
    }

    @Test
    fun refuelTrackerNavHost_verifyStartDestination() {
        navController.assertCurrentRouteName(FuelStopListDestination.route)
    }

    // Bottom navigation tests
    @Test
    fun refuelTrackerNavHost_clickAll_navigatesToListScreen() {
        composeTestRule.navigateToListScreen()
        navController.assertCurrentRouteName(FuelStopListDestination.route)
    }

    @Test
    fun refuelTrackerNavHost_clickCalendar_navigatesToCalendarScreen() {
        composeTestRule.navigateToCalendarScreen()
        navController.assertCurrentRouteName(FuelStopCalendarDestination.route)
    }

    @Test
    fun refuelTrackerNavHost_clickStatistics_navigatesToStatisticsScreen() {
        composeTestRule.navigateToStatisticsScreen()
        navController.assertCurrentRouteName(StatisticsHomeDestination.route)
    }

    // Top navigation tests
    @Test
    fun refuelTrackerNavHost_clickSettingsOnListScreen_navigatesToSettingsScreen() {
        composeTestRule.navigateToSettingsScreen()
        navController.assertCurrentRouteName(SettingsDestination.route)
    }

    @Test
    fun refuelTrackerNavHost_clickSettingsOnCalendarScreen_navigatesToSettingsScreen() {
        composeTestRule.navigateToCalendarScreen()
        composeTestRule.navigateToSettingsScreen()
        navController.assertCurrentRouteName(SettingsDestination.route)
    }

    @Test
    fun refuelTrackerNavHost_clickSettingsOnStatisticsScreen_navigatesToSettingsScreen() {
        composeTestRule.navigateToStatisticsScreen()
        composeTestRule.navigateToSettingsScreen()
        navController.assertCurrentRouteName(SettingsDestination.route)
    }

    @Test
    fun refuelTrackerNavHost_clickSettingsOnEntryScreen_navigatesToSettingsScreen() {
        composeTestRule.navigateToAddEntryScreen()
        composeTestRule.navigateToSettingsScreen()
        navController.assertCurrentRouteName(SettingsDestination.route)
    }

    // Back navigation ability tests
    @Test
    fun refuelTrackerNavHost_verifyBackNavigationNotShownOnListScreen() {
        composeTestRule.onNodeWithContentDescription(R.string.nav_back_button_description)
            .assertDoesNotExist()
    }

    @Test
    fun refuelTrackerNavHost_verifyBackNavigationNotShownOnCalendarScreen() {
        composeTestRule.navigateToCalendarScreen()
        composeTestRule.onNodeWithContentDescription(R.string.nav_back_button_description)
            .assertDoesNotExist()
    }

    @Test
    fun refuelTrackerNavHost_verifyBackNavigationNotShownOnStatisticsScreen() {
        composeTestRule.navigateToStatisticsScreen()
        composeTestRule.onNodeWithContentDescription(R.string.nav_back_button_description)
            .assertDoesNotExist()
    }

    @Test
    fun refuelTrackerNavHost_verifyBackNavigationShownOnEntryScreen() {
        composeTestRule.navigateToAddEntryScreen()
        composeTestRule.onNodeWithContentDescription(R.string.nav_back_button_description)
            .assertExists()
    }

    @Test
    fun refuelTrackerNavHost_verifyBackNavigationShownOnSettingsScreen() {
        composeTestRule.navigateToSettingsScreen()
        composeTestRule.onNodeWithContentDescription(R.string.nav_back_button_description)
            .assertExists()
    }

    // Settings navigation ability tests
    @Test
    fun refuelTrackerNavHost_verifySettingsNavigationShownOnListScreen() {
        composeTestRule.navigateToListScreen()
        composeTestRule.onNodeWithContentDescription(R.string.settings_button_icon_description)
            .assertExists()
    }

    @Test
    fun refuelTrackerNavHost_verifySettingsNavigationShownOnCalendarScreen() {
        composeTestRule.navigateToCalendarScreen()
        composeTestRule.onNodeWithContentDescription(R.string.settings_button_icon_description)
            .assertExists()
    }

    @Test
    fun refuelTrackerNavHost_verifySettingsNavigationShownOnStatisticsScreen() {
        composeTestRule.navigateToStatisticsScreen()
        composeTestRule.onNodeWithContentDescription(R.string.settings_button_icon_description)
            .assertExists()
    }

    @Test
    fun refuelTrackerNavHost_verifySettingsNavigationShownOnEntryScreen() {
        composeTestRule.navigateToAddEntryScreen()
        composeTestRule.onNodeWithContentDescription(R.string.settings_button_icon_description)
            .assertExists()
    }

    @Test
    fun refuelTrackerNavHost_verifySettingsNavigationNotShownOnSettingsScreen() {
        composeTestRule.navigateToSettingsScreen()
        composeTestRule.onNodeWithContentDescription(R.string.settings_button_icon_description)
            .assertDoesNotExist()
    }

    // Add entry navigation ability tests
    @Test
    fun refuelTrackerNavHost_verifyAddEntryNavigationShownOnListScreen() {
        composeTestRule.navigateToListScreen()
        composeTestRule.onNodeWithContentDescription(R.string.add_fuel_stop_button_description)
            .assertExists()
    }

    @Test
    fun refuelTrackerNavHost_verifyAddEntryNavigationShownOnCalendarScreen() {
        composeTestRule.navigateToCalendarScreen()
        composeTestRule.onNodeWithContentDescription(R.string.add_fuel_stop_button_description)
            .assertExists()
    }

    @Test
    fun refuelTrackerNavHost_verifyAddEntryNavigationNotShownOnStatisticsScreen() {
        composeTestRule.navigateToStatisticsScreen()
        composeTestRule.onNodeWithContentDescription(R.string.add_fuel_stop_button_description)
            .assertDoesNotExist()
    }

    @Test
    fun refuelTrackerNavHost_verifyAddEntryNavigationNotShownOnEntryScreen() {
        composeTestRule.navigateToAddEntryScreen()
        composeTestRule.onNodeWithContentDescription(R.string.add_fuel_stop_button_description)
            .assertDoesNotExist()
    }

    @Test
    fun refuelTrackerNavHost_verifyAddEntryNavigationNotShownOnSettingsScreen() {
        composeTestRule.navigateToSettingsScreen()
        composeTestRule.onNodeWithContentDescription(R.string.add_fuel_stop_button_description)
            .assertDoesNotExist()
    }

    // Back navigation tests
    @Test
    fun refuelTrackerNavHost_clickBackOnEntryScreenOpenedFromListScreen_navigatesToListScreen() {
        composeTestRule.navigateToListScreen()
        composeTestRule.navigateToAddEntryScreen()
        composeTestRule.performNavigateUp()
        navController.assertCurrentRouteName(FuelStopListDestination.route)
    }

    @Test
    fun refuelTrackerNavHost_clickBackOnEntryScreenOpenedFromCalendarScreen_navigatesToCalendarScreen() {
        composeTestRule.navigateToCalendarScreen()
        composeTestRule.navigateToAddEntryScreen()
        composeTestRule.performNavigateUp()
        navController.assertCurrentRouteName(FuelStopCalendarDestination.route)
    }

    @Test
    fun refuelTrackerNavHost_clickBackOnSettingsScreenOpenedFromListScreen_navigatesToListScreen() {
        composeTestRule.navigateToListScreen()
        composeTestRule.navigateToSettingsScreen()
        composeTestRule.performNavigateUp()
        navController.assertCurrentRouteName(FuelStopListDestination.route)
    }

    @Test
    fun refuelTrackerNavHost_clickBackOnSettingsScreenOpenedFromCalendarScreen_navigatesToCalendarScreen() {
        composeTestRule.navigateToCalendarScreen()
        composeTestRule.navigateToSettingsScreen()
        composeTestRule.performNavigateUp()
        navController.assertCurrentRouteName(FuelStopCalendarDestination.route)
    }

    @Test
    fun refuelTrackerNavHost_clickBackOnSettingsScreenOpenedFromStatisticsScreen_navigatesToStatisticsScreen() {
        composeTestRule.navigateToStatisticsScreen()
        composeTestRule.navigateToSettingsScreen()
        composeTestRule.performNavigateUp()
        navController.assertCurrentRouteName(StatisticsHomeDestination.route)
    }

    @Test
    fun refuelTrackerNavHost_clickBackOnSettingsScreenOpenedFromEntryScreen_navigatesToEntryScreen() {
        composeTestRule.navigateToAddEntryScreen()
        composeTestRule.navigateToSettingsScreen()
        composeTestRule.performNavigateUp()
        navController.assertCurrentRouteName(FuelStopEntryDestination.route)
    }

    // Add entry navigation tests
    @Test
    fun refuelTrackerNavHost_clickAddEntryOnListScreen_navigatesToEntryScreen() {
        composeTestRule.navigateToListScreen()
        composeTestRule.navigateToAddEntryScreen()
        navController.assertCurrentRouteName(FuelStopEntryDestination.route)
    }

    @Test
    fun refuelTrackerNavHost_clickAddEntryOnCalendarScreen_navigatesToEntryScreen() {
        composeTestRule.navigateToCalendarScreen()
        composeTestRule.navigateToAddEntryScreen()
        navController.assertCurrentRouteName(FuelStopEntryDestination.route)
    }
}