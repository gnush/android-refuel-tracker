package org.refueltracker.ui.statistic

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.refueltracker.CommonBottomAppBar
import org.refueltracker.CommonTopAppBar
import org.refueltracker.R
import org.refueltracker.data.FuelStopAverageValues
import org.refueltracker.data.FuelStopSumValues
import org.refueltracker.ui.Config
import org.refueltracker.ui.RefuelTrackerViewModelProvider
import org.refueltracker.ui.extensions.displaySign
import org.refueltracker.ui.extensions.defaultText
import org.refueltracker.ui.extensions.valueChangeColor
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
        StatisticsBody(
            uiState = viewModel.uiState,
            paddingValues = innerPadding
        )
    }
}

@Composable
private fun StatisticsBody(
    modifier: Modifier = Modifier,
    uiState: StatisticsHomeUiState = StatisticsHomeUiState(),
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    Column(modifier = modifier.padding(paddingValues)) {
        AverageFuelStatisticsCard(
            currentHeading = R.string.current_month_heading,
            currentStats = uiState.currentMonthAvg,
            previousHeading = R.string.previous_month_heading,
            previousStats = uiState.previousMonthAvg
        )
        AverageFuelStatisticsCard(
            currentHeading = R.string.current_year_heading,
            currentStats = uiState.currentYearAvg,
            previousHeading = R.string.previous_year_heading,
            previousStats = uiState.previousYearAvg
        )
        AllTimeAverageFuelStatisticsCard(
            heading = R.string.all_time_average_heading,
            averages = uiState.allStopsAvg,
            sums = uiState.allStopsSum
        )
    }
}

@Composable
private fun AllTimeAverageFuelStatisticsCard(
    @StringRes heading: Int,
    averages: FuelStopAverageValues,
    sums: FuelStopSumValues,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.card_elevation)
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(R.dimen.padding_small),
                bottom = dimensionResource(R.dimen.padding_small)
            ),
        shape = RectangleShape
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
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ValueText(
                    value = sums.price,
                    prefix = {
                        Icon(
                            imageVector = Icons.Filled.Functions,
                            contentDescription = stringResource(R.string.sum_aggregate_icon_description)
                        )
                    },
                    suffix = Config.DISPLAY_CURRENCY_SIGN
                )
                ValueText(
                    value = sums.volume,
                    prefix = {
                        Icon(
                            imageVector = Icons.Filled.Functions,
                            contentDescription = stringResource(R.string.sum_aggregate_icon_description)
                        )
                    },
                    suffix = Config.DISPLAY_VOLUME_SIGN
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = dimensionResource(R.dimen.padding_small),
                        bottom = dimensionResource(R.dimen.padding_small)
                    ),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ValueText(
                    value = averages.price,
                    prefix = { Text("∅") },
                    suffix = Config.DISPLAY_CURRENCY_SIGN
                )
                ValueText(
                    value = averages.volume,
                    prefix = { Text("∅") },
                    suffix = Config.DISPLAY_VOLUME_SIGN
                )
                ValueText(
                    value = averages.pricePerVolume,
                    prefix = { Text("∅") },
                    suffix = "${Config.DISPLAY_CURRENCY_SIGN}/${Config.DISPLAY_VOLUME_SIGN}"
                )
            }
        }
    }
}

@Composable
private fun AverageFuelStatisticsCard(
    @StringRes currentHeading: Int,
    @StringRes previousHeading: Int,
    currentStats: FuelStopAverageValues,
    previousStats: FuelStopAverageValues,
    modifier: Modifier = Modifier,
    @StringRes diffHeading: Int? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(R.dimen.padding_small),
                bottom = dimensionResource(R.dimen.padding_small)
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.card_elevation)
        ),
        shape = RectangleShape
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
                stats = currentStats-previousStats,
                isValueDiff = true,
                heading = diffHeading
            )
        }
    }
}

@Composable
private fun AverageValueColumn(
    stats: FuelStopAverageValues,
    modifier: Modifier = Modifier,
    @StringRes heading: Int? = null,
    isValueDiff: Boolean = false
) {
    Column(modifier = modifier) {
        Text(if (heading != null) stringResource(heading) else "")
        ValueText(
            value = stats.price,
            prefix = { Text("∅") },
            suffix = Config.DISPLAY_CURRENCY_SIGN,
            isValueDiff = isValueDiff
        )
        ValueText(
            value = stats.volume,
            prefix = { Text("∅") },
            suffix = Config.DISPLAY_VOLUME_SIGN,
            isValueDiff = isValueDiff
        )
        ValueText(
            value = stats.pricePerVolume,
            prefix = { Text("∅") },
            suffix = "${Config.DISPLAY_CURRENCY_SIGN}/${Config.DISPLAY_VOLUME_SIGN}",
            isValueDiff = isValueDiff
        )
    }
}

@Composable
private fun ValueText(
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
            Text(text = if (value.displaySign.isEmpty())
                            value.defaultText
                        else
                            "${value.displaySign} ${value.abs().defaultText}",
                 color = value.valueChangeColor
            )
        else
            Text(value.defaultText)
        Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))
        Text(suffix)
    }
}

@Preview(showBackground = true)
@Composable
private fun AllTimeFuelStatisticsCardPreview() {
    val ppv = BigDecimal("1.679")
    val vol = BigDecimal("23.87")
    val price = (ppv*vol).setScale(Config.DECIMAL_PLACES_DEFAULT, RoundingMode.HALF_UP)

    RefuelTrackerTheme {
        AllTimeAverageFuelStatisticsCard(
            heading = R.string.all_time_average_heading,
            averages = FuelStopAverageValues(ppv, vol, price),
            sums = FuelStopSumValues(BigDecimal("456.45"), BigDecimal("248.76"))
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FuelStatisticsCardPreview() {
    val ppv1 = BigDecimal("1.769")
    val vol1 = BigDecimal("76.87")
    val price1 = ppv1*vol1

    val ppv2 = BigDecimal("1.579")
    val vol2 = BigDecimal("85.32")
    val price2 = ppv2*vol2

    RefuelTrackerTheme {
        AverageFuelStatisticsCard(
            currentHeading = R.string.current_month_heading,
            previousHeading = R.string.previous_month_heading,
            currentStats = FuelStopAverageValues(ppv1, vol1, price1),
            previousStats = FuelStopAverageValues(ppv2, vol2, price2)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StatisticsBodyPreview() {
    RefuelTrackerTheme {
        StatisticsBody()
    }
}
