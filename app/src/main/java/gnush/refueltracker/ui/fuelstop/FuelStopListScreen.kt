package gnush.refueltracker.ui.fuelstop

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import gnush.refueltracker.CommonBottomAppBar
import gnush.refueltracker.CommonTopAppBar
import gnush.refueltracker.R
import gnush.refueltracker.data.FuelStop
import gnush.refueltracker.ui.Config
import gnush.refueltracker.ui.RefuelTrackerViewModelProvider
import gnush.refueltracker.ui.extensions.currencyText
import gnush.refueltracker.ui.extensions.ratioText
import gnush.refueltracker.ui.extensions.volumeText
import gnush.refueltracker.ui.navigation.BottomNavigationDestination
import gnush.refueltracker.ui.theme.FuelCardShape
import gnush.refueltracker.ui.theme.RefuelTrackerTheme
import java.math.BigDecimal

object FuelStopListDestination: BottomNavigationDestination {
    override val route: String = "fuel_stop_list_home"
    @StringRes override val titleRes: Int = R.string.fuel_stop_list_screen

    override val icon: ImageVector = Icons.AutoMirrored.Filled.List
    @StringRes override val iconDescriptionRes: Int = R.string.nav_bar_list_button_description
    @StringRes override val labelRes: Int = R.string.nav_bar_list_button_label
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelStopListScreen(
    navigateTo: (String) -> Unit,
    navigateToFuelStopEntry: () -> Unit,
    navigateToFuelStopEdit: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FuelStopListViewModel = viewModel(factory = RefuelTrackerViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CommonTopAppBar(
                title = stringResource(FuelStopListDestination.titleRes),
                canNavigateUp = false,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            CommonBottomAppBar(
                currentDestination = FuelStopListDestination,
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
        FuelStopList(
            fuelStops = uiState.fuelStops,
            onFuelStopClick = { navigateToFuelStopEdit(it.id) },
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding
        )
    }
}

@Composable
fun FuelStopList(
    fuelStops: List<FuelStop>,
    onFuelStopClick: (FuelStop) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(fuelStops) {
            FuelStopListItem(
                fuelStop = it,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
                    .clickable { onFuelStopClick(it) }
            )
        }
    }
}

@Composable
private fun FuelStopListItem(fuelStop: FuelStop, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.card_elevation)
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = FuelCardShape
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_tiny))
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(fuelStop.station)
                Spacer(Modifier.weight(1f))
                FuelStopDateTime(fuelStop.day, fuelStop.time)
            }
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(fuelStop.fuelSort)
                Spacer(Modifier.weight(1f))
                Text("${fuelStop.totalVolume.volumeText} ${Config.DISPLAY_VOLUME_SIGN}")
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("${fuelStop.pricePerVolume.ratioText} ${Config.DISPLAY_CURRENCY_SIGN}/${Config.DISPLAY_VOLUME_SIGN}")
                Spacer(Modifier.weight(1f))
                Text("${fuelStop.totalPrice.currencyText} ${Config.DISPLAY_CURRENCY_SIGN}")
            }
        }
    }
}

@Composable
private fun FuelStopDateTime(day: LocalDate, time: LocalTime?) {
    Column (
        horizontalAlignment = Alignment.End
    ) {
        Text(day.format(Config.DATE_FORMAT))
        if (time != null)
            Text(time.format(Config.TIME_FORMAT))
    }
}

@Preview(showBackground = true)
@Composable
private fun FuelStopListPreview() {
    val stop1PPV = BigDecimal("1.599")
    val stop1V = BigDecimal("20.01")
    val stop2PPV = BigDecimal("1.799")
    val stop2V = BigDecimal("34.56")

    RefuelTrackerTheme {
        FuelStopList(
            fuelStops = listOf(
                FuelStop(
                    station = "Fuel Station",
                    day = LocalDate(2000, 1, 1),
                    fuelSort = "E10",
                    pricePerVolume = stop1PPV,
                    totalVolume = stop1V.div(stop1PPV),
                    totalPrice = stop1V
                ),
                FuelStop(
                    station = "Other Fuel Station",
                    day = LocalDate(2011, 11, 30),
                    time = LocalTime(12, 1),
                    fuelSort = "E5",
                    pricePerVolume = stop2PPV,
                    totalVolume = stop2V/stop2PPV,
                    totalPrice = stop2V
                )
            ),
            onFuelStopClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FuelStopListNoTimeItemPreview() {
    val pricePerVolume = BigDecimal("1.599")
    val totalPrice = BigDecimal("20.01")

    RefuelTrackerTheme {
        FuelStopListItem(
            FuelStop(
                station = "Fuel Station",
                day = LocalDate(2000, 1, 1),
                fuelSort = "E10",
                pricePerVolume = pricePerVolume,
                totalVolume = totalPrice.div(pricePerVolume),
                totalPrice = totalPrice
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FuelStopListTimeItemPreview() {
    val pricePerVolume = BigDecimal("1.799")
    val totalPrice = BigDecimal("34.56")

    RefuelTrackerTheme {
        FuelStopListItem(
            FuelStop(
                station = "Fuel Station",
                day = LocalDate(2011, 11, 30),
                time = LocalTime(12, 1),
                fuelSort = "E5",
                pricePerVolume = pricePerVolume,
                totalVolume = totalPrice/pricePerVolume,
                totalPrice = totalPrice
            )
        )
    }
}
