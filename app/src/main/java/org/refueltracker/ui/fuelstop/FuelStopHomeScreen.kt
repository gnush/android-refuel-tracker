package org.refueltracker.ui.fuelstop

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import org.refueltracker.CommonTopAppBar
import org.refueltracker.R
import org.refueltracker.data.FuelStop
import org.refueltracker.ui.Config
import org.refueltracker.ui.navigation.NavigationDestination
import org.refueltracker.ui.theme.RefuelTrackerTheme
import java.math.BigDecimal

object FuelStopHomeDestination: NavigationDestination {
    override val route: String = "home"
    @StringRes override val titleRes: Int = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelStopHomeScreen(
    navigateToFuelStopEntry: () -> Unit,
    navigateToFuelStopUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CommonTopAppBar(
                title = stringResource(FuelStopHomeDestination.titleRes),
                canNavigateUp = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToFuelStopEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_fuel_stop_button_description)
                )
            }
        }
    ) { innerPadding ->
        FuelStopList(
            fuelStops = listOf(), // TODO: get from ViewModel state
            onFuelStopClick = { navigateToFuelStopUpdate(it.id) },
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                Text("${fuelStop.totalVolume} ${fuelStop.volumeSign}")
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("${fuelStop.pricePerVolume} ${fuelStop.currencySign}/${fuelStop.volumeSign}")
                Spacer(Modifier.weight(1f))
                Text("${fuelStop.totalPrice} ${fuelStop.currencySign}")
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
            Text(time.toString())
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
                    volumeSign = "L",
                    currencySign = "€",
                    pricePerVolume = stop1PPV,
                    totalVolume = stop1V.div(stop1PPV),
                    totalPrice = stop1V
                ),
                FuelStop(
                    station = "Other Fuel Station",
                    day = LocalDate(2011, 11, 30),
                    time = LocalTime(12, 1),
                    fuelSort = "E5",
                    volumeSign = "L",
                    currencySign = "€",
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
                volumeSign = "L",
                currencySign = "€",
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
                volumeSign = "L",
                currencySign = "€",
                pricePerVolume = pricePerVolume,
                totalVolume = totalPrice/pricePerVolume,
                totalPrice = totalPrice
            )
        )
    }
}
