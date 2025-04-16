package gnush.refueltracker.ui.fuelstop

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import gnush.refueltracker.CommonTopAppBar
import gnush.refueltracker.R
import gnush.refueltracker.ui.Config
import gnush.refueltracker.ui.DropDownSelection
import gnush.refueltracker.ui.data.FuelStopDetails
import gnush.refueltracker.ui.data.FuelStopUiState
import gnush.refueltracker.ui.RefuelTrackerViewModelProvider
import gnush.refueltracker.ui.data.DropDownItemsUiState
import gnush.refueltracker.ui.data.EntryUserPreferences
import gnush.refueltracker.ui.dialog.PickDateDialog
import gnush.refueltracker.ui.dialog.PickTimeDialDialog
import gnush.refueltracker.ui.extensions.updateBasedOnPricePerVolume
import gnush.refueltracker.ui.extensions.updateBasedOnTotalPrice
import gnush.refueltracker.ui.extensions.updateBasedOnTotalVolume
import gnush.refueltracker.ui.navigation.NavigationDestination
import gnush.refueltracker.ui.theme.RefuelTrackerTheme

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
            dropDownItems = uiState.dropDownItems,
            userPreferences = uiState.userPreferences,
            modifier = Modifier.fillMaxWidth(),
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

@Composable
private fun FuelStopInputForm(
    fuelStopDetails: FuelStopDetails,
    onValueChange: (FuelStopDetails) -> Unit,
    dropDownItems: DropDownItemsUiState,
    userPreferences: EntryUserPreferences,
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
                    mostRecentItems = dropDownItems.stationRecentDropDownItems,
                    mostUsedItems = dropDownItems.stationUsedDropDownItems,
                    defaultSelection = userPreferences.dropDownFilter,
                    onItemSelected = { onValueChange(fuelStopDetails.copy(station = it)) },
                )
            }
        )
        FormTextField(
            value = fuelStopDetails.fuelSort,
            onValueChange = { onValueChange(fuelStopDetails.copy(fuelSort = it)) },
            labelId = R.string.fuel_stop_sort_form_label,
            icon = {
                FormTextFieldDropDownMenu(
                    mostRecentItems = dropDownItems.fuelSortRecentDropDownItems,
                    mostUsedItems = dropDownItems.fuelSortUsedDropDownItems,
                    defaultSelection = userPreferences.dropDownFilter,
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
                    Text(userPreferences.signs.currency)
                    Text("/${userPreferences.signs.volume}")
                }
            }
        )
        FormTextField(
            value = fuelStopDetails.totalVolume,
            onValueChange = { onValueChange(fuelStopDetails.updateBasedOnTotalVolume(it)) },
            labelId = R.string.fuel_stop_total_volume_form_label,
            hasDecimalKeyboard = true,
            icon = { Text(userPreferences.signs.volume) }
        )
        FormTextField(
            value = fuelStopDetails.totalPrice,
            onValueChange = { onValueChange(fuelStopDetails.updateBasedOnTotalPrice(it)) },
            labelId = R.string.fuel_stop_total_paid_form_label,
            hasDecimalKeyboard = true,
            icon = { Text(userPreferences.signs.currency) }
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
    mostRecentItems: List<String>,
    mostUsedItems: List<String>,
    defaultSelection: DropDownSelection,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

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
            FormTextFieldDropDownMenuBody(
                mostRecentItems = mostRecentItems,
                mostUsedItems = mostUsedItems,
                defaultSelection = defaultSelection,
                onItemSelected = {
                    expanded = false
                    onItemSelected(it)
                }
            )
        }
    }
}

@Composable
private fun FormTextFieldDropDownMenuBody(
    mostRecentItems: List<String>,
    mostUsedItems: List<String>,
    defaultSelection: DropDownSelection,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selection by remember { mutableStateOf(defaultSelection) }

    Column(modifier) {
        DropDownFilter(
            onFilterClick = { selection = it },
            selection = selection
        )
        HorizontalDivider()
        when (selection) {
            DropDownSelection.MostRecent -> mostRecentItems
            DropDownSelection.MostUsed -> mostUsedItems
        }.forEach {
            DropdownMenuItem(
                text = { Text(it) },
                onClick = { onItemSelected(it) }
            )
        }
    }
}

@Composable
private fun DropDownFilter(
    onFilterClick: (DropDownSelection) -> Unit,
    selection: DropDownSelection,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(
            start = dimensionResource(R.dimen.padding_small),
            end = dimensionResource(R.dimen.padding_small),
            top = dimensionResource(R.dimen.padding_small),
            bottom = dimensionResource(R.dimen.padding_small)
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(stringResource(R.string.entry_screen_drop_down_selection))
        Spacer(Modifier.weight(1f))
        DropDownFilterButton(
            onClick = { onFilterClick(DropDownSelection.MostUsed) },
            buttonText = R.string.entry_screen_drop_down_selection_most_used,
            isSelected = selection == DropDownSelection.MostUsed
        )
        Spacer(Modifier.width(dimensionResource(R.dimen.padding_medium)))
        DropDownFilterButton(
            onClick = { onFilterClick(DropDownSelection.MostRecent) },
            buttonText = R.string.entry_screen_drop_down_selection_most_recent,
            isSelected = selection == DropDownSelection.MostRecent
        )
    }
}

@Composable
private fun DropDownFilterButton(
    onClick: () -> Unit,
    isSelected: Boolean,
    @StringRes buttonText: Int,
) {
    Text(
        text = stringResource(buttonText),
        textDecoration = if (isSelected) TextDecoration.Underline else null,
        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else Color.Unspecified,
        modifier = Modifier.clickable(onClick = onClick)
    )
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
            onFuelStopValueChange = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FormTextFieldDropDownMenuBodyPreview() {
    RefuelTrackerTheme {
        FormTextFieldDropDownMenuBody(
            mostRecentItems = emptyList(),
            mostUsedItems = listOf("First", "Second", "Third"),
            onItemSelected = {},
            defaultSelection = DropDownSelection.MostUsed
        )
    }
}