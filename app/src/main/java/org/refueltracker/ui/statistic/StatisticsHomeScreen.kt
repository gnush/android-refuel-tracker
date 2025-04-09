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
import org.refueltracker.CommonBottomAppBar
import org.refueltracker.CommonTopAppBar
import org.refueltracker.R
import org.refueltracker.ui.Config
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
//  - add statistics for favorite fuel sort etc?

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
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Text("Current Month")
            Text("Current Year")
            Text("Complete")
        }
    }
}

@Composable
fun AllTimeAverageFuelStatisticsCard(
    @StringRes heading: Int,
    averagePricePerVolume: BigDecimal,
    averageVolume: BigDecimal,
    averagePrice: BigDecimal,
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
                modifier = Modifier.fillMaxWidth()
                    .padding(
                        top = dimensionResource(R.dimen.padding_small),
                        bottom = dimensionResource(R.dimen.padding_small)
                    ),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AverageValue(
                    averagePricePerVolume,
                    "${Config.DISPLAY_CURRENCY_SIGN}/${Config.DISPLAY_VOLUME_SIGN}"
                )
                AverageValue(
                    averageVolume,
                    Config.DISPLAY_VOLUME_SIGN
                )
                AverageValue(
                    averagePrice,
                    Config.DISPLAY_CURRENCY_SIGN
                )
            }
        }
    }
}

@Composable
fun AverageFuelStatisticsCard(
    @StringRes currentHeading: Int,
    @StringRes previousHeading: Int,
    @StringRes diffHeading: Int,
    currentAvgPricePerVolume: BigDecimal,
    currentAvgVolume: BigDecimal,
    currentAvgPrice: BigDecimal,
    previousAvgPricePerVolume: BigDecimal,
    previousAvgVolume: BigDecimal,
    previousAvgPrice: BigDecimal,
    modifier: Modifier = Modifier
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
            Column {
                Text(stringResource(currentHeading))
                AverageValue(currentAvgPricePerVolume,"${Config.DISPLAY_CURRENCY_SIGN}/${Config.DISPLAY_VOLUME_SIGN}")
                AverageValue(currentAvgVolume, Config.DISPLAY_VOLUME_SIGN)
                AverageValue(currentAvgPrice, Config.DISPLAY_CURRENCY_SIGN)
            }
            Column {
                Text(stringResource(previousHeading))
                AverageValue(previousAvgPricePerVolume, "${Config.DISPLAY_CURRENCY_SIGN}/${Config.DISPLAY_VOLUME_SIGN}")
                AverageValue(previousAvgVolume, Config.DISPLAY_VOLUME_SIGN)
                AverageValue(previousAvgPrice, Config.DISPLAY_CURRENCY_SIGN)
            }
            Column {
                val diffAvgPricePerVolume = previousAvgPricePerVolume-currentAvgPricePerVolume
                val diffAvgVolume = previousAvgVolume-currentAvgVolume
                val diffAvgPrice = previousAvgPrice-currentAvgPrice
                Text(stringResource(diffHeading))
                AverageValue(
                    value = diffAvgPricePerVolume.abs(),
                    suffix = "${Config.DISPLAY_CURRENCY_SIGN}/${Config.DISPLAY_VOLUME_SIGN}",
                    color = valueChangeColor(diffAvgPricePerVolume),
                    sign = sign(diffAvgPricePerVolume)
                )
                AverageValue(
                    value = diffAvgVolume.abs(),
                    suffix = Config.DISPLAY_VOLUME_SIGN,
                    color = valueChangeColor(diffAvgVolume),
                    sign = sign(diffAvgVolume)
                )
                AverageValue(
                    value = diffAvgPrice.abs(),
                    suffix = Config.DISPLAY_CURRENCY_SIGN,
                    color = valueChangeColor(diffAvgPrice),
                    sign = sign(diffAvgPrice)
                )
            }
        }
    }
}

private fun sign(value: BigDecimal): String =
    if (value.signum() < 0)
        "-"
    else if (value.signum() > 0)
        "+"
    else
        ""

private fun valueChangeColor(value: BigDecimal): Color? =
    if (value.signum() < 0)
        Config.DECREASE_COLOR
    else if (value.signum() > 0)
        Config.INCREASE_COLOR
    else
        null

@Composable
private fun AverageValue(
    value: BigDecimal,
    suffix: String,
    color: Color? = null,
    modifier: Modifier = Modifier,
    sign: String = "",
) {
    Row(
        modifier = modifier.padding(
                bottom = dimensionResource(R.dimen.padding_tiny),
                top = dimensionResource(R.dimen.padding_tiny)
        ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("∅")
        Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))
        if (color == null)
            Text(if (sign.isEmpty()) value.toString() else "$sign $value")
        else
            Text(if (sign.isEmpty()) value.toString() else "$sign $value", color = color)
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
            diffHeading = R.string.diff_average_heading,
            currentAvgPricePerVolume = ppv1,
            currentAvgVolume = vol1,
            currentAvgPrice = price1,
            previousAvgPricePerVolume = ppv2,
            previousAvgVolume = vol2,
            previousAvgPrice = price2,
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
            averagePricePerVolume = ppv,
            averageVolume = vol,
            averagePrice = price
        )
    }
}