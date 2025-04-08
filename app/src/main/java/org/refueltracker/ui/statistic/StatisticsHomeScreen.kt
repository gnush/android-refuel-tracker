package org.refueltracker.ui.statistic

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import kotlinx.datetime.LocalDate
import org.refueltracker.CommonBottomAppBar
import org.refueltracker.CommonTopAppBar
import org.refueltracker.R
import org.refueltracker.ui.calendar.CalendarView
import org.refueltracker.ui.navigation.BottomNavigationDestination

object StatisticsHomeDestination: BottomNavigationDestination {
    override val route: String = "statistics_home"
    @StringRes override val titleRes: Int = R.string.app_name

    override val icon: ImageVector = Icons.Filled.BarChart
    @StringRes override val iconDescriptionRes: Int = R.string.nav_bar_stat_button_description
    @StringRes override val labelRes: Int = R.string.nav_bar_stat_button_label
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsHomeScreen(
    navigateTo: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.fuel_stop_statistics_screen),
                canNavigateUp = false,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            CommonBottomAppBar(
                onNavigationItemClicked = navigateTo
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        CalendarView(
            onClickNext = {},
            onClickPrev = {},
            onCellClick = {},
            modifier = Modifier.padding(innerPadding),
            dates = listOf(
                Pair(LocalDate(2001, 11, 1), false),
                Pair(LocalDate(2001, 11, 2), false),
                Pair(LocalDate(2001, 11, 3), true),
                Pair(LocalDate(2001, 11, 4), true),
                Pair(LocalDate(2001, 11, 5), false),
                Pair(LocalDate(2001, 11, 6), false),
                Pair(LocalDate(2001, 11, 7), false),
                Pair(LocalDate(2001, 11, 8), true),
                Pair(LocalDate(2001, 11, 9), false)
            ),
            startFromSunday = false
        )
    }
}