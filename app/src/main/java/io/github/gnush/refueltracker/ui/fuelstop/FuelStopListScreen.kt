package io.github.gnush.refueltracker.ui.fuelstop

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import io.github.gnush.refueltracker.CommonBottomAppBar
import io.github.gnush.refueltracker.CommonTopAppBar
import io.github.gnush.refueltracker.R
import io.github.gnush.refueltracker.data.FuelStop
import io.github.gnush.refueltracker.ui.Config
import io.github.gnush.refueltracker.ui.RefuelTrackerViewModelProvider
import io.github.gnush.refueltracker.ui.data.DefaultSigns
import io.github.gnush.refueltracker.ui.data.FuelStopListItem
import io.github.gnush.refueltracker.ui.data.UserFormats
import io.github.gnush.refueltracker.ui.extensions.format
import io.github.gnush.refueltracker.ui.navigation.BottomNavigationDestination
import io.github.gnush.refueltracker.ui.theme.FuelCardShape
import io.github.gnush.refueltracker.ui.theme.RefuelTrackerTheme
import java.math.BigDecimal
import java.text.NumberFormat

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
    navigateToSettings: () -> Unit,
    navigateToAbout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FuelStopListViewModel = viewModel(factory = RefuelTrackerViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val uiState by viewModel.uiState.collectAsState()

    Log.d("ME", "default min fraction = ${NumberFormat.getInstance().minimumFractionDigits}")
    Log.d("ME", "default max fraction = ${NumberFormat.getInstance().maximumFractionDigits}")
    Log.d("ME", "currency min fraction = ${NumberFormat.getCurrencyInstance().minimumFractionDigits}")
    Log.d("ME", "currency max fraction = ${NumberFormat.getCurrencyInstance().maximumFractionDigits}")

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CommonTopAppBar(
                title = stringResource(FuelStopListDestination.titleRes),
                onSettingsClick = navigateToSettings,
                onAboutClick = navigateToAbout,
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
            signs = uiState.signs,
            formats = uiState.formats,
            onFuelStopClick = { navigateToFuelStopEdit(it.id) },
            onFuelStopLongPress = viewModel::toggleSelection,
            onDeleteSelection = viewModel::deleteSelection,
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FuelStopList(
    fuelStops: List<FuelStopListItem>,
    signs: DefaultSigns,
    formats: UserFormats,
    onFuelStopClick: (FuelStop) -> Unit,
    onFuelStopLongPress: (Long) -> Unit,
    onDeleteSelection: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val haptics = LocalHapticFeedback.current

    Box(
        modifier.padding(contentPadding),

    ) {
        LazyColumn(
            modifier = Modifier.align(Alignment.TopCenter),
        ) {
            items(
                items = fuelStops,
                key = { it.stop.id }
            ) {
                FuelStopListItemCard(
                    item = it,
                    signs = signs,
                    formats = formats,
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_small))
                        .combinedClickable(
                            onClick = { onFuelStopClick(it.stop) },
                            onClickLabel = stringResource(R.string.list_edit_label),
                            onLongClick = {
                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                onFuelStopLongPress(it.stop.id)
                            },
                            onLongClickLabel = stringResource(R.string.list_select_label)
                        )
                )
            }
        }
        if (fuelStops.any { it.isSelected })
            Row(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(dimensionResource(R.dimen.padding_tiny))
            ) {
                Button(
                    onDeleteSelection
                ) {
                    Icon(
                        Icons.Outlined.Delete,
                        contentDescription = stringResource(R.string.delete_icon_description),
                        Modifier.padding(end = dimensionResource(R.dimen.padding_tiny))
                    )
                    Text(stringResource(R.string.remove_button))
                }
            }
    }
}

@Composable
private fun FuelStopListItemCard(
    item: FuelStopListItem,
    signs: DefaultSigns,
    formats: UserFormats,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.card_elevation)
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = FuelCardShape
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_tiny))
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(item.stop.station)
                Spacer(Modifier.weight(1f))
                FuelStopDateTime(item.stop.day, item.stop.time, formats)
            }
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(item.stop.fuelSort)
                Spacer(Modifier.weight(1f))
                Text("${item.stop.totalVolume.format(formats.volume)} ${signs.volume}")
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("${item.stop.pricePerVolume.format(formats.ratio)} ${signs.currency}/${signs.volume}")
                Spacer(Modifier.weight(1f))
                Text("${item.stop.totalPrice.format(formats.currency)} ${signs.currency}")
            }
        }
    }
}

@Composable
private fun FuelStopDateTime(day: LocalDate, time: LocalTime?, formats: UserFormats) {
    Column (
        horizontalAlignment = Alignment.End
    ) {
        Text(day.format(formats.date.get))
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
                FuelStopListItem(
                    FuelStop(
                        station = "Fuel Station",
                        day = LocalDate(2000, 1, 1),
                        fuelSort = "E10",
                        pricePerVolume = stop1PPV,
                        totalVolume = stop1V.div(stop1PPV),
                        totalPrice = stop1V,
                        volume = "L",
                        currency = "€"
                    )
                ),
                FuelStopListItem(
                    FuelStop(
                        station = "Other Fuel Station",
                        day = LocalDate(2011, 11, 30),
                        time = LocalTime(12, 1),
                        fuelSort = "E5",
                        pricePerVolume = stop2PPV,
                        totalVolume = stop2V/stop2PPV,
                        totalPrice = stop2V,
                        volume = "L",
                        currency = "€"
                    )
                )
            ),
            signs = DefaultSigns(
                currency = "€",
                volume = "L"
            ),
            formats = UserFormats(),
            onFuelStopClick = {},
            onFuelStopLongPress = {},
            onDeleteSelection = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FuelStopListNoTimeItemPreview() {
    val pricePerVolume = BigDecimal("1.599")
    val totalPrice = BigDecimal("20.01")

    RefuelTrackerTheme {
        FuelStopListItemCard(
            item = FuelStopListItem(
                FuelStop(
                    station = "Fuel Station",
                    day = LocalDate(2000, 1, 1),
                    fuelSort = "E10",
                    pricePerVolume = pricePerVolume,
                    totalVolume = totalPrice.div(pricePerVolume),
                    totalPrice = totalPrice,
                    volume = "L",
                    currency = "€"
                )
            ),
            signs = DefaultSigns(
                currency = "€",
                volume = "L"
            ),
            formats = UserFormats(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FuelStopListTimeItemPreview() {
    val pricePerVolume = BigDecimal("1.799")
    val totalPrice = BigDecimal("34.56")

    RefuelTrackerTheme {
        FuelStopListItemCard(
            item = FuelStopListItem(
                FuelStop(
                    station = "Fuel Station",
                    day = LocalDate(2011, 11, 30),
                    time = LocalTime(12, 1),
                    fuelSort = "E5",
                    pricePerVolume = pricePerVolume,
                    totalVolume = totalPrice/pricePerVolume,
                    totalPrice = totalPrice,
                    volume = "L",
                    currency = "€"
                )
            ),
            signs = DefaultSigns(
                currency = "€",
                volume = "L"
            ),
            formats = UserFormats(),
        )
    }
}
