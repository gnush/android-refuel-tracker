package org.refueltracker.ui.fuelstop

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.vector.ImageVector
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
import org.refueltracker.ui.extensions.updateBasedOnPricePerVolume
import org.refueltracker.ui.extensions.updateBasedOnTotalPrice
import org.refueltracker.ui.extensions.updateBasedOnTotalVolume
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
            stationDropDownItems = uiState.stationDropDownItems,
            fuelSortDropDownItems = uiState.fuelSortDropDownItems,
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
    stationDropDownItems: List<String>,
    fuelSortDropDownItems: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        modifier = modifier
    ) {
        var showTimeDialog by remember { mutableStateOf(false) }
        var showDateDialog by remember { mutableStateOf(false) }

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

        FormTextField(
            value = fuelStopDetails.day,
            onValueChange = { onValueChange(fuelStopDetails.copy(day = it)) },
            labelId = R.string.fuel_stop_day_form_label,
            icon = {
                FormTextFieldButton(
                    onClick = { showDateDialog = true },
                    icon = Icons.Default.DateRange,
                    iconDescription = R.string.pick_date_button_description
                )
            }
        )
        FormTextField(
            value = fuelStopDetails.time?.format(Config.TIME_FORMAT) ?: "",
            onValueChange = { onValueChange(fuelStopDetails.copy(time = it)) },
            labelId = R.string.fuel_stop_time_form_label,
            icon = {
                FormTextFieldButton(
                    onClick = { showTimeDialog = true },
                    icon = Icons.Default.AccessTime,
                    iconDescription = R.string.pick_time_button_description
                )
            }
        )
        FormTextField(
            value = fuelStopDetails.station,
            onValueChange = { onValueChange(fuelStopDetails.copy(station = it)) },
            labelId = R.string.fuel_stop_station_form_label,
            icon = {
                FormTextFieldDropDownMenu(
                    menuItems = stationDropDownItems,
                    onItemSelected = { onValueChange(fuelStopDetails.copy(station = it)) }
                )
            }
        )
        FormTextField(
            value = fuelStopDetails.fuelSort,
            onValueChange = { onValueChange(fuelStopDetails.copy(fuelSort = it)) },
            labelId = R.string.fuel_stop_sort_form_label,
            icon = {
                FormTextFieldDropDownMenu(
                    menuItems = fuelSortDropDownItems,
                    onItemSelected = { onValueChange(fuelStopDetails.copy(fuelSort = it)) }
                )
            }
        )
        FormTextField(
            value = fuelStopDetails.pricePerVolume,
            onValueChange = { onValueChange(fuelStopDetails.updateBasedOnPricePerVolume(it)) },
            labelId = R.string.fuel_stop_price_per_volume_form_label,
            hasDecimalKeyboard = true,
            icon = {
                Row {
                    Text(Config.DISPLAY_CURRENCY_SIGN)
                    Text("/${Config.DISPLAY_VOLUME_SIGN}")
                }
            }
        )
        FormTextField(
            value = fuelStopDetails.totalVolume,
            onValueChange = { onValueChange(fuelStopDetails.updateBasedOnTotalVolume(it)) },
            labelId = R.string.fuel_stop_total_volume_form_label,
            hasDecimalKeyboard = true,
            icon = { Text(Config.DISPLAY_VOLUME_SIGN) }
        )
        FormTextField(
            value = fuelStopDetails.totalPrice,
            onValueChange = { onValueChange(fuelStopDetails.updateBasedOnTotalPrice(it)) },
            labelId = R.string.fuel_stop_total_paid_form_label,
            hasDecimalKeyboard = true,
            icon = { Text(Config.DISPLAY_CURRENCY_SIGN) }
        )
    }
}

@Composable
private fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes labelId: Int,
    modifier: Modifier = Modifier,
    hasDecimalKeyboard: Boolean = false,
    isIconLeading: Boolean = false,
    icon: @Composable (() -> Unit)? = null
) {
    val colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(labelId)) },
        colors = colors,
        singleLine = true,
        keyboardOptions =
            if (hasDecimalKeyboard) KeyboardOptions(keyboardType = KeyboardType.Decimal)
            else KeyboardOptions.Default,
        leadingIcon = if (isIconLeading) icon else null,
        trailingIcon = if (!isIconLeading) icon else null,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun FormTextFieldButton(
    onClick: () -> Unit,
    icon: ImageVector,
    @StringRes iconDescription: Int
) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(iconDescription)
        )
    }
}

@Composable
private fun FormTextFieldDropDownMenu(
    menuItems: List<String>,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) { // TODO: why does it move to the side if value of text field this is embedded to changes?
    //       replicate with minimal example
    var expanded: Boolean by remember { mutableStateOf(false) }
    Log.d("ME", "expanded = $expanded")
    Box(modifier = modifier) {
        FormTextFieldButton(
            onClick = { expanded = !expanded },
            icon = Icons.Default.ArrowDropDown,
            iconDescription = R.string.drop_down_button_description
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            menuItems.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        expanded = false
                        onItemSelected(it)
                    }
                )
            }
        }
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
                isValid = true,
            ),
            onSaveClick = {},
            onFuelStopValueChange = {}
        )
    }
}