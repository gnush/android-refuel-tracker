package io.github.gnush.refueltracker.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import io.github.gnush.refueltracker.data.FuelStop
import io.github.gnush.refueltracker.ui.data.DefaultSigns
import io.github.gnush.refueltracker.ui.data.UserFormats
import io.github.gnush.refueltracker.ui.fuelstop.FuelStopList
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

class FuelStopListScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val fuelStops = listOf(
        FuelStop(
            id = 1,
            station = "Station1",
            fuelSort = "Sort1",
            currency = "Currency",
            volume = "Volume",
            pricePerVolume = BigDecimal.ONE,
            totalVolume = BigDecimal.ONE,
            totalPrice = BigDecimal.ONE,
            day = LocalDate(2000, 1, 1),
            time = LocalTime(12, 0)
        ),
        FuelStop(
            id = 2,
            station = "Station",
            fuelSort = "Sort",
            currency = "Currency",
            volume = "Volume",
            pricePerVolume = BigDecimal.ONE,
            totalVolume = BigDecimal.ONE,
            totalPrice = BigDecimal.ONE,
            day = LocalDate(2000, 1, 2),
            time = LocalTime(14, 30)
        )
    )

    @Test
    fun fuelStopListScreen_verifyContent() {
        composeTestRule.setContent {
            FuelStopList(
                fuelStops = fuelStops,
                signs = DefaultSigns(),
                formats = UserFormats(),
                onFuelStopClick = {}
            )
        }

        fuelStops.forEach {
            composeTestRule.onNodeWithText(it.station, useUnmergedTree = true)
                .onParent()
                .assertIsDisplayed()
                .assertHasClickAction()
        }
    }
}