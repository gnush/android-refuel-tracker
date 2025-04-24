package io.github.gnush.refueltracker

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import io.github.gnush.refueltracker.ui.calendar.CalendarUiState
import io.github.gnush.refueltracker.ui.extensions.monthOfYearAbbreviationId
import io.github.gnush.refueltracker.ui.statistic.StatisticsBody
import io.github.gnush.refueltracker.ui.statistic.StatisticsHomeUiState
import kotlinx.datetime.Month
import org.junit.Rule
import org.junit.Test

class StatisticsHomeScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun statisticsHomeScreen_verifyContent() {
        val displayYear = 1000
        val displayMonth = CalendarUiState(Month(1), 2000)

        composeTestRule.setContent {
            StatisticsBody(
                onNavigateLeftMonth = {},
                onNavigateRightMonth = {},
                onNavigateLeftYear = {},
                onNavigateRightYear = {},
                uiState = StatisticsHomeUiState(
                    monthCalendar = displayMonth,
                    year = displayYear
                )
            )
        }

        composeTestRule.onNodeWithText(displayYear.toString())
            .assertIsDisplayed()

        composeTestRule.onNodeWithStringId(displayMonth.month.monthOfYearAbbreviationId)
            .assertIsDisplayed()

        composeTestRule.onNodeWithStringId(R.string.all_time_average_heading)
            .assertIsDisplayed()
    }
}