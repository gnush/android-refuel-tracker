package org.refueltracker.ui.fuelstop

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import org.refueltracker.CommonTopAppBar
import org.refueltracker.R
import org.refueltracker.ui.Config
import org.refueltracker.ui.data.FuelStopDetails
import org.refueltracker.ui.data.FuelStopUiState
import org.refueltracker.ui.RefuelTrackerViewModelProvider
import org.refueltracker.ui.dialog.PickDateDialog
import org.refueltracker.ui.dialog.PickTimeDialDialog
import org.refueltracker.ui.navigation.NavigationDestination
import org.refueltracker.ui.theme.RefuelTrackerTheme

object FuelStopEntryDestination: NavigationDestination {
    override val route: String = "fuel_stop_entry"
    @StringRes override val titleRes: Int = R.string.fuel_stop_entry_screen
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelStopEntryScreen(
    onNavigateUp: () -> Unit,
    onSaveClickNavigateTo: () -> Unit,
    modifier: Modifier = Modifier,
    canNavigateUp: Boolean = true,
    viewModel: FuelStopEntryViewModel = viewModel(factory = RefuelTrackerViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(FuelStopEntryDestination.titleRes),
                canNavigateUp = canNavigateUp,
                onNavigateUp = onNavigateUp
            )
        },
        modifier = modifier
    ) { innerPadding ->
        FuelStopEntryBody(
            uiState = viewModel.uiState,
            onFuelStopValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveFuelStop()
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

@Composable
fun FuelStopEntryBody(
    uiState: FuelStopUiState,
    onFuelStopValueChange: (FuelStopDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_large)),
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        FuelStopInputForm(
            fuelStopDetails = uiState.details,
            onValueChange = onFuelStopValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = uiState.isValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_button))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FuelStopInputForm(
    fuelStopDetails: FuelStopDetails,
    onValueChange: (FuelStopDetails) -> Unit,
    modifier: Modifier = Modifier,
    inputEnabled: Boolean = true
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        modifier = modifier
    ) {
        var showTimeDialog by remember { mutableStateOf(false) }
        var showDateDialog by remember { mutableStateOf(false) }

        val colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
        )
        val modifier = Modifier.fillMaxWidth()
        val decimalKeyboard = KeyboardOptions(keyboardType = KeyboardType.Decimal)

        if (showTimeDialog) {
            PickTimeDialDialog(
                onConfirm = {
                    onValueChange(fuelStopDetails.copy(time = it))
                    showTimeDialog = false
                },
                onDismiss = { showTimeDialog = false }
            )
        }

        if (showDateDialog) {
            PickDateDialog(
                onDateSelected = {
                    onValueChange(fuelStopDetails.copy(day = it))
                    showDateDialog = false
                },
                onDismiss = { showDateDialog = false }
            )
        }

        OutlinedTextField(
            value = fuelStopDetails.day,
            onValueChange = { onValueChange(fuelStopDetails.copy(day = it)) },
            label = { Text(stringResource(R.string.fuel_stop_day_form_label)) },
            colors = colors,
            modifier = modifier,
            enabled = inputEnabled,
            singleLine = true,
            trailingIcon = {
                IconButton(
                    onClick = { showDateDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = stringResource(R.string.pick_time_button_description)
                    )
                }
            }
        )
        OutlinedTextField(
            value = fuelStopDetails.time?.format(Config.TIME_FORMAT) ?: "",
            onValueChange = { onValueChange(fuelStopDetails.copy(time = it)) },
            label = { Text(stringResource(R.string.fuel_stop_time_form_label)) },
            colors = colors,
            modifier = modifier,
            enabled = inputEnabled,
            singleLine = true,
            trailingIcon = {
                IconButton(
                    onClick = { showTimeDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.pick_time_button_description)
                    )
                }
            }
        )
        OutlinedTextField(
            value = fuelStopDetails.station,
            onValueChange = { onValueChange(fuelStopDetails.copy(station = it)) },
            label = { Text(stringResource(R.string.fuel_stop_station_form_label)) },
            colors = colors,
            modifier = modifier,
            enabled = inputEnabled,
            singleLine = true
        )
        OutlinedTextField(
            value = fuelStopDetails.fuelSort,
            onValueChange = { onValueChange(fuelStopDetails.copy(fuelSort = it)) },
            label = { Text(stringResource(R.string.fuel_stop_sort_form_label)) },
            colors = colors,
            modifier = modifier,
            enabled = inputEnabled,
            singleLine = true
        )
        OutlinedTextField(
            value = fuelStopDetails.pricePerVolume,
            onValueChange = { onValueChange(fuelStopDetails.copy(pricePerVolume = it)) },
            label = { Text(stringResource(R.string.fuel_stop_price_per_volume_form_label)) },
            colors = colors,
            modifier = modifier,
            enabled = inputEnabled,
            singleLine = true,
            keyboardOptions = decimalKeyboard,
            trailingIcon = {
                Row {
                    Text(Config.DISPLAY_CURRENCY_SIGN)
                    Text("/${Config.DISPLAY_VOLUME_SIGN}")
                }
            }
        )
        OutlinedTextField(
            value = fuelStopDetails.totalVolume,
            onValueChange = { onValueChange(fuelStopDetails.copy(totalVolume = it)) },
            label = { Text(stringResource(R.string.fuel_stop_total_volume_form_label)) },
            colors = colors,
            modifier = modifier,
            enabled = inputEnabled,
            singleLine = true,
            keyboardOptions = decimalKeyboard,
            trailingIcon = { Text(Config.DISPLAY_VOLUME_SIGN) }
        )
        OutlinedTextField(
            value = fuelStopDetails.totalPrice,
            onValueChange = { onValueChange(fuelStopDetails.copy(totalPrice = it)) },
            label = { Text(stringResource(R.string.fuel_stop_total_paid_form_label)) },
            colors = colors,
            modifier = modifier,
            enabled = inputEnabled,
            singleLine = true,
            keyboardOptions = decimalKeyboard,
            leadingIcon = { Text(Config.DISPLAY_CURRENCY_SIGN) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FuelStopEntryPreview() {
    RefuelTrackerTheme {
        FuelStopEntryBody(
            uiState = FuelStopUiState(
                details = FuelStopDetails(
                    station = "Station",
                    fuelSort = "E10",
                    pricePerVolume = "2.00",
                    totalVolume = "10",
                    totalPrice = "20",
                    day = "10.02.2007",
                    time = "07:33"
                ),
                isValid = true
            ),
            onSaveClick = {},
            onFuelStopValueChange = {}
        )
    }
}