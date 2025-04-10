package org.refueltracker.ui.statistic

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import org.refueltracker.CommonBottomAppBar
import org.refueltracker.CommonTopAppBar
import org.refueltracker.R
import org.refueltracker.data.FuelStopDecimalValues
import org.refueltracker.ui.Config
import org.refueltracker.ui.RefuelTrackerViewModelProvider
import org.refueltracker.ui.navigation.BottomNavigationDestination
import org.refueltracker.ui.theme.RefuelTrackerTheme
import java.math.BigDecimal
import java.math.RoundingMode

object StatisticsHomeDestination: BottomNavigationDestination {
    override val route: String = "statistics_home"
    @StringRes override val titleRes: Int = R.string.app_name

    override val icon: ImageVector = Icons.Filled.BarChart
    @StringRes override val iconDescriptionRes: Int = R.string.nav_bar_stat_button_description
    @StringRes override val labelRes: Int = R.string.nav_bar_stat_button_label
}

// TODO:
//  - add statistics for favourite fuel sort etc?

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsHomeScreen(
    navigateTo: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StatisticsHomeViewModel = viewModel(factory = RefuelTrackerViewModelProvider.Factory)
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
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            AverageFuelStatisticsCard(
                currentHeading = R.string.current_month_heading,
                currentStats = viewModel.uiState.currentMonthStops,
                previousHeading = R.string.previous_month_heading,
                previousStats = viewModel.uiState.previousMonthStops
            )
            AverageFuelStatisticsCard(
                currentHeading = R.string.current_year_heading,
                currentStats = viewModel.uiState.currentYearStops,
                previousHeading = R.string.previous_year_heading,
                previousStats = viewModel.uiState.previousYearStops
            )
            AllTimeAverageFuelStatisticsCard(
                heading = R.string.all_time_average_heading,
                stats = viewModel.uiState.allStops
            )
        }
    }
}

@Composable
fun AllTimeAverageFuelStatisticsCard(
    @StringRes heading: Int,
    stats: FuelStopDecimalValues,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.card_elevation)
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))) {
            Text(stringResource(heading))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = dimensionResource(R.dimen.padding_small),
                        bottom = dimensionResource(R.dimen.padding_small)
                    ),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AverageValueText(
                    value = stats.price,
                    prefix = { Text("∅") },
                    suffix = Config.DISPLAY_CURRENCY_SIGN
                )
                AverageValueText(
                    value = stats.volume,
                    prefix = { Text("∅") },
                    suffix = Config.DISPLAY_VOLUME_SIGN
                )
                AverageValueText(
                    value = stats.pricePerVolume,
                    prefix = { Text("∅") },
                    suffix = "${Config.DISPLAY_CURRENCY_SIGN}/${Config.DISPLAY_VOLUME_SIGN}"
                )
            }
        }
    }
}

@Composable
fun AverageFuelStatisticsCard(
    @StringRes currentHeading: Int,
    @StringRes previousHeading: Int,
    currentStats: FuelStopDecimalValues,
    previousStats: FuelStopDecimalValues,
    modifier: Modifier = Modifier,
    @StringRes diffHeading: Int? = null
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.card_elevation)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_large))
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            AverageValueColumn(heading = previousHeading, stats = previousStats)
            AverageValueColumn(heading = currentHeading, stats = currentStats)
            AverageValueColumn(
                stats = previousStats-currentStats,
                isValueDiff = true,
                heading = diffHeading
            )
        }
    }
}

@Composable
private fun AverageValueColumn(
    stats: FuelStopDecimalValues,
    modifier: Modifier = Modifier,
    @StringRes heading: Int? = null,
    isValueDiff: Boolean = false
) {
    Column(modifier = modifier) {
        Text(if (heading != null) stringResource(heading) else "")
        AverageValueText(
            value = stats.price,
            prefix = { Text("∅") },
            suffix = Config.DISPLAY_CURRENCY_SIGN,
            isValueDiff = isValueDiff
        )
        AverageValueText(
            value = stats.volume,
            prefix = { Text("∅") },
            suffix = Config.DISPLAY_VOLUME_SIGN,
            isValueDiff = isValueDiff
        )
        AverageValueText(
            value = stats.pricePerVolume,
            prefix = { Text("∅") },
            suffix = "${Config.DISPLAY_CURRENCY_SIGN}/${Config.DISPLAY_VOLUME_SIGN}",
            isValueDiff = isValueDiff
        )
    }
}

@Composable
private fun AverageValueText(
    value: BigDecimal,
    prefix: @Composable () -> Unit,
    suffix: String,
    modifier: Modifier = Modifier,
    isValueDiff: Boolean = false
) {
    Row(
        modifier = modifier.padding(
                bottom = dimensionResource(R.dimen.padding_tiny),
                top = dimensionResource(R.dimen.padding_tiny)
        ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        prefix()
        Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))
        if (isValueDiff)
            Text(
                text = if (value.sign().isEmpty()) value.toString() else "${value.sign()} ${value.abs()}",
                color = value.valueChangeColor()
            )
        else
            Text(value.toString())
        Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))
        Text(suffix)
    }
}

@Preview(showBackground = true)
@Composable
private fun FuelStatisticsCardPreview() {
    val ppv1 = BigDecimal("1.769")
    val vol1 = BigDecimal("76.87")
    val price1 = (ppv1*vol1).setScale(Config.CURRENCY_DECIMAL_PLACES_DEFAULT, RoundingMode.HALF_UP)

    val ppv2 = BigDecimal("1.579")
    val vol2 = BigDecimal("85.32")
    val price2 = (ppv2*vol2).setScale(Config.CURRENCY_DECIMAL_PLACES_DEFAULT, RoundingMode.HALF_UP)

    RefuelTrackerTheme {
        AverageFuelStatisticsCard(
            currentHeading = R.string.current_month_heading,
            previousHeading = R.string.previous_month_heading,
            currentStats = FuelStopDecimalValues(ppv1, vol1, price1),
            previousStats = FuelStopDecimalValues(ppv2, vol2, price2)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AllTimeFuelStatisticsCardPreview() {
    val ppv = BigDecimal("1.679")
    val vol = BigDecimal("476.87")
    val price = (ppv*vol).setScale(Config.CURRENCY_DECIMAL_PLACES_DEFAULT, RoundingMode.HALF_UP)

    RefuelTrackerTheme {
        AllTimeAverageFuelStatisticsCard(
            heading = R.string.all_time_average_heading,
            stats = FuelStopDecimalValues(ppv, vol, price)
        )
    }
}

private fun BigDecimal.sign(): String =
    if (signum() < 0)
        "-"
    else if (signum() > 0)
        "+"
    else
        ""

private fun BigDecimal.valueChangeColor(): Color =
    if (signum() < 0)
        Config.DECREASE_COLOR
    else if (signum() > 0)
        Config.INCREASE_COLOR
    else
        Color.Unspecified

private operator fun FuelStopDecimalValues.minus(other: FuelStopDecimalValues) = FuelStopDecimalValues(
    pricePerVolume = pricePerVolume - other.pricePerVolume,
    volume = volume - other.volume,
    price = price - other.price
)