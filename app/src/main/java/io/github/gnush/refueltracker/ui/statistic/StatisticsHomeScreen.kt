package io.github.gnush.refueltracker.ui.statistic

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.gnush.refueltracker.CommonBottomAppBar
import io.github.gnush.refueltracker.CommonTopAppBar
import io.github.gnush.refueltracker.R
import io.github.gnush.refueltracker.data.FuelStopAverageValues
import io.github.gnush.refueltracker.data.FuelStopSumValues
import io.github.gnush.refueltracker.ui.RefuelTrackerViewModelProvider
import io.github.gnush.refueltracker.ui.data.DefaultSigns
import io.github.gnush.refueltracker.ui.data.UserFormats
import io.github.gnush.refueltracker.ui.extensions.format
import io.github.gnush.refueltracker.ui.extensions.monthOfYearAbbreviationId
import io.github.gnush.refueltracker.ui.extensions.paddedDisplaySign
import io.github.gnush.refueltracker.ui.extensions.valueChangeColor
import io.github.gnush.refueltracker.ui.navigation.BottomNavigationDestination
import io.github.gnush.refueltracker.ui.theme.RefuelTrackerTheme
import java.math.BigDecimal
import java.text.NumberFormat

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
    navigateToSettings: () -> Unit,
    navigateToAbout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StatisticsHomeViewModel = viewModel(factory = RefuelTrackerViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.fuel_stop_statistics_screen),
                onSettingsClick = navigateToSettings,
                onAboutClick = navigateToAbout,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            CommonBottomAppBar(
                currentDestination = StatisticsHomeDestination,
                onNavigationItemClicked = navigateTo
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        StatisticsBody(
            uiState = viewModel.uiState,
            paddingValues = innerPadding,
            onNavigateLeftMonth = viewModel::navigatePreviousMonth,
            onNavigateRightMonth = viewModel::navigateNextMonth,
            onNavigateLeftYear = viewModel::navigatePreviousYear,
            onNavigateRightYear = viewModel::navigateNextYear,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
fun StatisticsBody(
    modifier: Modifier = Modifier,
    onNavigateLeftMonth: () -> Unit,
    onNavigateRightMonth: () -> Unit,
    onNavigateLeftYear: () -> Unit,
    onNavigateRightYear: () -> Unit,
    uiState: StatisticsHomeUiState = StatisticsHomeUiState(),
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    Column(modifier = modifier.padding(paddingValues)) {
        AverageFuelStatisticsCard(
            currentHeading = {
                AverageStatisticsMonthHeading(
                    uiState.monthCalendar.month.monthOfYearAbbreviationId,
                    uiState.monthCalendar.year
                )
            },
            currentStats = uiState.currentMonthAvg,
            previousHeading = {
                val previousCalendar = uiState.monthCalendar.previousMonth()
                AverageStatisticsMonthHeading(
                    previousCalendar.month.monthOfYearAbbreviationId,
                    previousCalendar.year
                )
            },
            previousStats = uiState.previousMonthAvg,
            onNavigateLeft = onNavigateLeftMonth,
            onNavigateRight = onNavigateRightMonth,
            signs = uiState.signs,
            formats = uiState.formats
        )
        AverageFuelStatisticsCard(
            currentHeading = { Text(uiState.year.toString()) },
            currentStats = uiState.currentYearAvg,
            previousHeading = { Text((uiState.year-1).toString()) },
            previousStats = uiState.previousYearAvg,
            onNavigateLeft = onNavigateLeftYear,
            onNavigateRight = onNavigateRightYear,
            signs = uiState.signs,
            formats = uiState.formats
        )
        AllTimeAverageFuelStatisticsCard(
            heading = R.string.all_time_average_heading,
            averages = uiState.allStopsAvg,
            sums = uiState.allStopsSum,
            signs = uiState.signs,
            formats = uiState.formats
        )
    }
}

@Composable
private fun AverageStatisticsMonthHeading(
    @StringRes monthOfYearId: Int,
    year: Int
) {
    Row {
        Text(stringResource(monthOfYearId))
        Spacer(Modifier.width(dimensionResource(R.dimen.padding_tiny)))
        Text(year.toString())
    }
}

@Composable
private fun AllTimeAverageFuelStatisticsCard(
    @StringRes heading: Int,
    averages: FuelStopAverageValues,
    sums: FuelStopSumValues,
    signs: DefaultSigns,
    formats: UserFormats,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.card_elevation)
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RectangleShape,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(R.dimen.padding_small),
                bottom = dimensionResource(R.dimen.padding_small)
            )
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
                    format = formats.currency,
                    prefix = {
                        Icon(
                            imageVector = Icons.Filled.Functions,
                            contentDescription = stringResource(R.string.sum_aggregate_icon_description)
                        )
                    },
                    suffix = signs.currency
                )
                ValueText(
                    value = sums.volume,
                    format = formats.volume,
                    prefix = {
                        Icon(
                            imageVector = Icons.Filled.Functions,
                            contentDescription = stringResource(R.string.sum_aggregate_icon_description)
                        )
                    },
                    suffix = signs.volume
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
                    format = formats.currency,
                    prefix = { Text("∅") },
                    suffix = signs.currency
                )
                ValueText(
                    value = averages.volume,
                    format = formats.volume,
                    prefix = { Text("∅") },
                    suffix = signs.volume
                )
                ValueText(
                    value = averages.pricePerVolume,
                    format = formats.ratio,
                    prefix = { Text("∅") },
                    suffix = "${signs.currency}/${signs.volume}"
                )
            }
        }
    }
}

@Composable
private fun AverageFuelStatisticsCard(
    signs: DefaultSigns,
    formats: UserFormats,
    currentHeading: @Composable () -> Unit,
    previousHeading: @Composable () -> Unit,
    currentStats: FuelStopAverageValues,
    previousStats: FuelStopAverageValues,
    onNavigateLeft: () -> Unit,
    onNavigateRight: () -> Unit,
    modifier: Modifier = Modifier,
    diffHeading: @Composable (() -> Unit)? = null
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.card_elevation)
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RectangleShape,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(R.dimen.padding_small),
                bottom = dimensionResource(R.dimen.padding_small)
            )
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            NavigationButton(
                onClick = onNavigateLeft,
                icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                iconDescription = R.string.calendar_previous_button_description,
                modifier = Modifier.align(Alignment.CenterStart)
            )
            Row(
                modifier = Modifier
                    .padding(
                        top = dimensionResource(R.dimen.padding_large),
                        bottom = dimensionResource(R.dimen.padding_large)
                    )
                    .align(Alignment.Center),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                AverageValueColumn(
                    heading = previousHeading,
                    stats = previousStats,
                    signs = signs,
                    formats = formats
                )
                Spacer(Modifier.width(dimensionResource(R.dimen.padding_medium)))
                AverageValueColumn(
                    heading = currentHeading,
                    stats = currentStats,
                    signs = signs,
                    formats = formats
                )
                Spacer(Modifier.width(dimensionResource(R.dimen.padding_medium)))
                AverageValueColumn(
                    stats = currentStats-previousStats,
                    isValueDiff = true,
                    heading = diffHeading,
                    signs = signs,
                    formats = formats
                )
            }
            NavigationButton(
                onClick = onNavigateRight,
                icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                iconDescription = R.string.calendar_next_button_description,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}

@Composable
private fun NavigationButton(
    onClick: () -> Unit,
    icon: ImageVector,
    @StringRes iconDescription: Int,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(iconDescription)
        )
    }
}

@Composable
private fun AverageValueColumn(
    stats: FuelStopAverageValues,
    signs: DefaultSigns,
    formats: UserFormats,
    modifier: Modifier = Modifier,
    heading: @Composable (() -> Unit)? = null,
    isValueDiff: Boolean = false
) {
    Column(modifier = modifier) {
        heading?.invoke() ?: Text("") // TODO: how to properly align the card (and the elements), do we really need a grid?
        ValueText(
            value = stats.price,
            format = formats.currency,
            prefix = { Text("∅") },
            suffix = signs.currency,
            isValueDiff = isValueDiff
        )
        ValueText(
            value = stats.volume,
            format = formats.volume,
            prefix = { Text("∅") },
            suffix = signs.volume,
            isValueDiff = isValueDiff
        )
        ValueText(
            value = stats.pricePerVolume,
            format = formats.ratio,
            prefix = { Text("∅") },
            suffix = "${signs.currency}/${signs.volume}",
            isValueDiff = isValueDiff
        )
    }
}

@Composable
private fun ValueText(
    value: BigDecimal,
    format: NumberFormat,
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
        if (!isValueDiff) {
            prefix()
            Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))
        }
        val sign = if (isValueDiff) value.paddedDisplaySign else ""
        Text(
            text = "$sign${value.abs().format(format)}",
            color = if (isValueDiff) value.valueChangeColor else Color.Unspecified
        )
        Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))
        Text(suffix)
    }
}

@Preview(showBackground = true)
@Composable
private fun AllTimeFuelStatisticsCardPreview() {
    val ppv = BigDecimal("1.679")
    val vol = BigDecimal("23.87")
    val price = ppv*vol

    RefuelTrackerTheme {
        AllTimeAverageFuelStatisticsCard(
            heading = R.string.all_time_average_heading,
            averages = FuelStopAverageValues(ppv, vol, price),
            sums = FuelStopSumValues(BigDecimal("456.45"), BigDecimal("248.76")),
            signs = DefaultSigns(
                volume = "L",
                currency = "€"
            ),
            formats = UserFormats()
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
            currentHeading = { Text(stringResource(R.string.current_month_heading)) },
            previousHeading = { Text(stringResource(R.string.previous_month_heading)) },
            currentStats = FuelStopAverageValues(ppv1, vol1, price1),
            previousStats = FuelStopAverageValues(ppv2, vol2, price2),
            onNavigateLeft = {},
            onNavigateRight = {},
            signs = DefaultSigns(
                volume = "L",
                currency = "€"
            ),
            formats = UserFormats()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StatisticsBodyPreview() {
    RefuelTrackerTheme {
        StatisticsBody(
            onNavigateLeftMonth = {},
            onNavigateRightMonth = {},
            onNavigateLeftYear = {},
            onNavigateRightYear = {}
        )
    }
}
