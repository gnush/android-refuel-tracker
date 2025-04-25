package io.github.gnush.refueltracker.ui.fuelstop

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import io.github.gnush.refueltracker.CommonTopAppBar
import io.github.gnush.refueltracker.R
import io.github.gnush.refueltracker.ui.RefuelTrackerViewModelProvider
import io.github.gnush.refueltracker.ui.navigation.NavigationDestination
import io.github.gnush.refueltracker.ui.theme.RefuelTrackerTheme

object FuelStopEditDestination: NavigationDestination {
    override val route = "fuel_stop_edit"
    override val titleRes: Int = R.string.fuel_stop_edit_screen
    const val FUEL_STOP_ID = "fuelStopId"
    val routeWithFuelStopId = "$route/{$FUEL_STOP_ID}"
    fun routeWithFuelStopId(id: Long): String = "$route/$id"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelStopEditScreen(
    onNavigateUp: () -> Unit,
    onSaveClickNavigateTo: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToAbout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FuelStopEntryViewModel = viewModel(factory = RefuelTrackerViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(FuelStopEditDestination.titleRes),
                onNavigateUp = onNavigateUp,
                onSettingsClick = navigateToSettings,
                onAboutClick = navigateToAbout
            )
        },
        modifier = modifier
    ) { innerPadding ->
        FuelStopEntryBody(
            uiState = viewModel.uiState,
            onFuelStopValueChange = viewModel::updateUiState,
            onPricePerVolumeChange = viewModel::updateBasedOnPricePerVolume,
            onVolumeChange = viewModel::updateBasedOnTotalVolume,
            onPriceChange = viewModel::updateBasedOnTotalPrice,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateFuelStop()
                    onSaveClickNavigateTo()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FuelStopEditScreenPreview() {
    RefuelTrackerTheme {
        FuelStopEditScreen(
            onNavigateUp = {},
            onSaveClickNavigateTo = {},
            navigateToSettings = {},
            navigateToAbout = {}
        )
    }
}