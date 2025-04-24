package io.github.gnush.refueltracker

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import io.github.gnush.refueltracker.ui.data.DefaultSigns
import io.github.gnush.refueltracker.ui.data.EntryUserPreferences
import io.github.gnush.refueltracker.ui.data.FuelStopDetails
import io.github.gnush.refueltracker.ui.data.FuelStopUiState
import io.github.gnush.refueltracker.ui.fuelstop.FuelStopEntryBody
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class FuelStopEntryScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun fuelStopEntryScreen_verifyContent() {
        val details = FuelStopDetails(
            station = "Station",
            fuelSort = "Sort",
            totalPrice = "1",
            totalVolume = "2",
            pricePerVolume = "0.5",
            day = "today",
            time = "now"
        )

        val preferences = EntryUserPreferences(
            signs = DefaultSigns(
                volume = "VOL",
                currency = "CUR"
            )
        )

        composeTestRule.setContent {
            FuelStopEntryBody(
                uiState = FuelStopUiState(
                    details = details,
                    isValid = false,
                    userPreferences = preferences
                ),
                onFuelStopValueChange = {},
                onPricePerVolumeChange = {},
                onVolumeChange = {},
                onPriceChange = {},
                onSaveClick = {},
            )
        }

        // Form elements correct
        composeTestRule.onNodeWithStringId(R.string.fuel_stop_day_form_label)
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
            .assertTextContains(details.day)

        composeTestRule.onNodeWithStringId(R.string.fuel_stop_time_form_label)
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
            .assertTextContains(details.time!!)

        composeTestRule.onNodeWithStringId(R.string.fuel_stop_station_form_label)
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
            .assertTextContains(details.station)

        composeTestRule.onNodeWithStringId(R.string.fuel_stop_sort_form_label)
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
            .assertTextContains(details.fuelSort)

        composeTestRule.onNodeWithStringId(R.string.fuel_stop_price_per_volume_form_label)
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
            .assertTextContains(details.pricePerVolume)
            .assertTextContains(preferences.signs.currency)
            .assertTextContains("/"+preferences.signs.volume)

        composeTestRule.onNodeWithStringId(R.string.fuel_stop_total_volume_form_label)
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
            .assertTextContains(details.totalVolume)
            .assertTextContains(preferences.signs.volume)

        composeTestRule.onNodeWithStringId(R.string.fuel_stop_total_paid_form_label)
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
            .assertTextContains(details.totalPrice)
            .assertTextContains(preferences.signs.currency)

        // Pick date button
        composeTestRule.onNodeWithContentDescription(R.string.pick_date_button_description)
            .assertIsDisplayed()
            .assertHasClickAction()

        // Pick time button
        composeTestRule.onNodeWithContentDescription(R.string.pick_time_button_description)
            .assertIsDisplayed()
            .assertHasClickAction()

        // Drop down menus
        composeTestRule.onAllNodesWithContentDescription(
            composeTestRule.activity.getString(R.string.drop_down_button_description), useUnmergedTree = true
        ).apply {
            assertEquals(2, fetchSemanticsNodes().size)

            fetchSemanticsNodes().forEachIndexed { i, _ ->
                get(i)
                    .assertIsDisplayed()
                    .onParent()
                    .assertHasClickAction()
                    .performClick()
            }
        }
        composeTestRule.onAllNodesWithStringId(R.string.entry_screen_drop_down_selection)
            .apply {
                fetchSemanticsNodes().forEachIndexed { i, _ ->
                    get(i).assertIsDisplayed()
                }
            }

        // Save button not enabled
        composeTestRule.onNodeWithStringId(R.string.save_button)
            .assertIsDisplayed()
            .assertHasClickAction()
            .assertIsNotEnabled()
    }
}