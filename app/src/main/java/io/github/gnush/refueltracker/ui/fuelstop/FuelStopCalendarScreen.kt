package io.github.gnush.refueltracker.ui.fuelstop

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.datetime.LocalDate
import io.github.gnush.refueltracker.CommonBottomAppBar
import io.github.gnush.refueltracker.CommonTopAppBar
import io.github.gnush.refueltracker.R
import io.github.gnush.refueltracker.data.FuelStop
import io.github.gnush.refueltracker.ui.RefuelTrackerViewModelProvider
import io.github.gnush.refueltracker.ui.calendar.CalendarUiState
import io.github.gnush.refueltracker.ui.calendar.CalendarView
import io.github.gnush.refueltracker.ui.navigation.BottomNavigationDestination

object FuelStopCalendarDestination: BottomNavigationDestination {
    override val route: String = "fuel_stop_calendar_home"
    @StringRes override val titleRes: Int = R.string.fuel_stop_calendar_screen

    override val icon: ImageVector = Icons.Filled.CalendarMonth
    @StringRes override val iconDescriptionRes: Int = R.string.nav_bar_cal_button_description
    @StringRes override val labelRes: Int = R.string.nav_bar_cal_button_label
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelStopCalendarScreen(
    navigateTo: (String) -> Unit,
    navigateToFuelStopEntry: () -> Unit,
    navigateToFuelStopEdit: (Long) -> Unit,
    navigateToSettings: () -> Unit,
    navigateToAbout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FuelStopCalendarViewModel = viewModel(factory = RefuelTrackerViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val uiState by viewModel.fuelStopsState.collectAsState()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CommonTopAppBar(
                title = stringResource(FuelStopCalendarDestination.titleRes),
                onSettingsClick = navigateToSettings,
                onAboutClick = navigateToAbout,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            CommonBottomAppBar(
                currentDestination = FuelStopCalendarDestination,
                onNavigationItemClicked = navigateTo,
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = navigateToFuelStopEntry,
                        shape = MaterialTheme.shapes.medium,
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.add_fuel_stop_button_description)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier.padding(innerPadding)) {
            FuelStopCalendar(
                uiState = uiState.calendar,
                fuelStopDates = uiState.fuelStops.map(FuelStop::day),
                onPreviousClick = viewModel::displayPreviousMonth,
                onNextClick = viewModel::displayNextMonth
            )
            FuelStopList(
                fuelStops = uiState.fuelStops,
                signs = uiState.signs,
                formats = uiState.formats,
                onFuelStopClick = { navigateToFuelStopEdit(it.id) }
            )
        }
    }
}

@Composable
fun FuelStopCalendar(
    uiState: CalendarUiState,
    fuelStopDates: List<LocalDate>,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CalendarView(
        firstDisplayMonth = uiState.month,
        firstDisplayYear = uiState.year,
        selectedDays = fuelStopDates,
        canNavigateMonth = true,
        onNextMonthClick = onNextClick,
        onPreviousMonthClick = onPreviousClick,
        startFromSunday = false,
        modifier = modifier
    )
}