package io.github.gnush.refueltracker

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule

fun <A: ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>
        .onNodeWithStringId(@StringRes id: Int): SemanticsNodeInteraction =
    onNodeWithText(activity.getString(id))

fun <A: ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>
        .onAllNodesWithStringId(@StringRes id: Int): SemanticsNodeInteractionCollection =
    onAllNodesWithText(activity.getString(id))

fun <A: ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>
        .onNodeWithContentDescription(@StringRes id: Int,
                                      substring: Boolean = false,
                                      ignoreCase: Boolean = false,
                                      useUnmergedTree: Boolean = false): SemanticsNodeInteraction =
    onNodeWithContentDescription(
        label = activity.getString(id),
        substring = substring,
        ignoreCase = ignoreCase,
        useUnmergedTree = useUnmergedTree
    )

fun <A: ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>
        .navigateToListScreen(): SemanticsNodeInteraction =
    onNodeWithContentDescription(R.string.nav_bar_list_button_description, useUnmergedTree = true)
        .performClick()

fun <A: ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>
        .navigateToCalendarScreen(): SemanticsNodeInteraction =
    onNodeWithContentDescription(R.string.nav_bar_cal_button_description, useUnmergedTree = true)
        .performClick()

fun <A: ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>
        .navigateToStatisticsScreen(): SemanticsNodeInteraction =
    onNodeWithContentDescription(R.string.nav_bar_stat_button_description, useUnmergedTree = true)
        .performClick()

fun <A: ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>
        .navigateToAddEntryScreen(): SemanticsNodeInteraction =
    onNodeWithContentDescription(R.string.add_fuel_stop_button_description, useUnmergedTree = true)
        .performClick()

fun <A: ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>
        .navigateToSettingsScreen(): SemanticsNodeInteraction =
    onNodeWithContentDescription(R.string.settings_button_icon_description)
        .performClick()

fun <A: ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>
        .performNavigateUp(): SemanticsNodeInteraction =
    onNodeWithContentDescription(R.string.nav_back_button_description)
        .performClick()