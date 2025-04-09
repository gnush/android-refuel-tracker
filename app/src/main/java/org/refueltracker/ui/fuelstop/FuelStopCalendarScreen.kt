package org.refueltracker.ui.fuelstop

import androidx.annotation.StringRes
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import org.refueltracker.CommonBottomAppBar
import org.refueltracker.CommonTopAppBar
import org.refueltracker.R
import org.refueltracker.ui.navigation.BottomNavigationDestination

object FuelStopCalendarDestination: BottomNavigationDestination {
    override val route: String = "fuel_stop_calendar_home"
    @StringRes override val titleRes: Int = R.string.app_name

    override val icon: ImageVector = Icons.Filled.CalendarMonth
    @StringRes override val iconDescriptionRes: Int = R.string.nav_bar_cal_button_description
    @StringRes override val labelRes: Int = R.string.nav_bar_cal_button_label
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelStopCalendarScreen(
    navigateTo: (String) -> Unit,
    navigateToFuelStopEntry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CommonTopAppBar(
                title = stringResource(FuelStopCalendarDestination.titleRes),
                canNavigateUp = false,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            CommonBottomAppBar(
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
        Text("", modifier.padding(innerPadding))
    }
}