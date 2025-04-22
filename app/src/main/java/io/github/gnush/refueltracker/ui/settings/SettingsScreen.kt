package io.github.gnush.refueltracker.ui.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.gnush.refueltracker.CommonTopAppBar
import io.github.gnush.refueltracker.R
import io.github.gnush.refueltracker.ui.DropDownSelection
import io.github.gnush.refueltracker.ui.RefuelTrackerViewModelProvider
import io.github.gnush.refueltracker.ui.createNumberFormat
import io.github.gnush.refueltracker.ui.navigation.NavigationDestination
import io.github.gnush.refueltracker.ui.theme.RefuelTrackerTheme
import java.text.NumberFormat

object ConfigDestination: NavigationDestination {
    override val route: String = "config"
    @StringRes override val titleRes: Int = R.string.config_screen
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel(factory = RefuelTrackerViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            CommonTopAppBar(
                title = stringResource(ConfigDestination.titleRes),
                onNavigateUp = onNavigateUp,
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // TODO: date/time formats encoding: enum/sealed class Predefined values or by pattern
            //       add composable for drop down choice
//            SettingsCategory(
//                title = R.string.settings_category_date_formats_title
//            )
//            SettingsCategoryDivider()
            SettingsCategory(title = R.string.settings_category_number_formats_title) {
                FormattedNumber(
                    label = R.string.settings_example_number_display_label,
                    separateLargeNumbers = uiState.separateThousands,
                    largeNumberSeparator = uiState.thousandsSeparator,
                    decimalPlaces = uiState.currencyDecimalPlaces
                )
                SwitchPreference(
                    label = R.string.settings_use_large_number_separator_label,
                    value = uiState.separateThousands,
                    onValueChange = viewModel::saveSeparateLargeNumbers
                )
                if (uiState.separateThousands)
                    SingleInputPreference(
                        label = R.string.settings_large_number_separator_places_label,
                        preference = uiState.thousandsSeparator,
                        onValueChange = viewModel::saveLargeNumbersSeparatorPlaces,
                        hasNumericKeyboard = true
                    )
                SingleInputPreference(
                    label = R.string.settings_currency_decimal_places_label,
                    preference = uiState.currencyDecimalPlaces,
                    onValueChange = viewModel::saveCurrencyDecimalPlaces,
                    hasNumericKeyboard = true
                )
                SingleInputPreference(
                    label = R.string.settings_volume_decimal_places_label,
                    preference = uiState.volumeDecimalPlaces,
                    onValueChange = viewModel::saveVolumeDecimalPlaces,
                    hasNumericKeyboard = true
                )
                SingleInputPreference(
                    label = R.string.settings_ratio_decimal_places_label,
                    preference = uiState.ratioDecimalPlaces,
                    onValueChange = viewModel::saveRatioDecimalPlaces,
                    hasNumericKeyboard = true
                )
            }
            SettingsCategoryDivider()
            SettingsCategory(title = R.string.settings_category_symbols_title) {
                SingleInputPreference(
                    label = R.string.settings_currency_sign_label,
                    preference = Preference(uiState.currencySign, true),
                    onValueChange = viewModel::saveCurrencySign
                )
                SingleInputPreference(
                    label = R.string.settings_volume_sign_label,
                    preference = Preference(uiState.volumeSign, true),
                    onValueChange = viewModel::saveVolumeSign
                )
            }
            SettingsCategoryDivider()
            SettingsCategory(
                title = R.string.settings_category_drop_down_title
            ) {
                SingleInputPreference(
                    label = R.string.settings_num_drop_down_elements_label,
                    preference = uiState.numDropDownElements,
                    onValueChange = viewModel::saveNumberOfDropDownElements
                )
                DefaultDropDownFilterPreference(
                    label = R.string.settings_default_drop_down_filter_label,
                    currentSelected = uiState.defaultDropDownFilter,
                    onItemSelected = viewModel::saveInitialDropDownFilter
                )
            }
        }
    }
}

@Composable
private fun SettingsCategory(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit) ={}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.headlineSmall
        )
        content()
    }
}

@Composable
private fun SettingsCategoryDivider(
    modifier: Modifier = Modifier
) {
    HorizontalDivider(
        thickness = 2.dp,
        modifier = modifier
            .padding(
                top = dimensionResource(R.dimen.padding_tiny),
                bottom = dimensionResource(R.dimen.padding_tiny)
            )
    )
}

@Composable
private fun CenteredPreferenceRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .padding(
                start = dimensionResource(R.dimen.padding_medium),
                end = dimensionResource(R.dimen.padding_small),
                top = dimensionResource(R.dimen.padding_tiny),
                bottom = dimensionResource(R.dimen.padding_tiny)
            ),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

@Composable
private fun FormattedNumber(
    @StringRes label: Int,
    separateLargeNumbers: Boolean,
    largeNumberSeparator: Preference,
    decimalPlaces: Preference,
    modifier: Modifier = Modifier
) {
    CenteredPreferenceRow(modifier = modifier) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(Modifier.weight(1f))
        if (largeNumberSeparator.isValid && decimalPlaces.isValid)
            Text(
                text = createNumberFormat(
                    separateLargeNumbers,
                    largeNumberSeparator.value.toInt(),
                    decimalPlaces.value.toInt()
                ).format(1234.5678),
                style = MaterialTheme.typography.labelMedium
            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DefaultDropDownFilterPreference(
    @StringRes label: Int,
    currentSelected: DropDownSelection,
    onItemSelected: (DropDownSelection) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    CenteredPreferenceRow(modifier) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(Modifier.weight(1f))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            TextField(
                value = stringResource(currentSelected.displayText),
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                modifier = Modifier
                    .width(dimensionResource(R.dimen.settings_text_field_width))
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listOf(DropDownSelection.MostUsed, DropDownSelection.MostRecent).forEach {
                    DropdownMenuItem(
                        text = { Text(stringResource(it.displayText)) },
                        onClick = {
                            expanded = false
                            onItemSelected(it)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}

@Composable
private fun SwitchPreference(
    @StringRes label: Int,
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    CenteredPreferenceRow(modifier) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(Modifier.weight(1f))
        Switch(
            checked = value,
            onCheckedChange = onValueChange
        )
    }
}

@Composable
private fun SingleInputPreference(
    @StringRes label: Int,
    preference: Preference,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hasNumericKeyboard: Boolean = false
) {
    val keyboardOptions =
        if (hasNumericKeyboard)
            KeyboardOptions(keyboardType = KeyboardType.Number)
        else
            KeyboardOptions.Default

    CenteredPreferenceRow(modifier) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(Modifier.weight(1f))
        TextField(
            value = preference.value,
            isError = !preference.isValid,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardOptions = keyboardOptions,
            modifier = Modifier.width(dimensionResource(R.dimen.settings_text_field_width))
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StringPreferencePreview() {
    RefuelTrackerTheme {
        SingleInputPreference(
            label = R.string.settings_currency_sign_label,
            preference = Preference(value = "€"),
            onValueChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsCategoryPreview() {
    RefuelTrackerTheme {
        SettingsCategory(
            title = R.string.settings_category_symbols_title
        ) {
            SingleInputPreference(
                label = R.string.settings_currency_sign_label,
                preference = Preference(value = "€", isValid = false),
                onValueChange = {}
            )
            SingleInputPreference(
                label = R.string.settings_volume_sign_label,
                preference = Preference(value = "L"),
                onValueChange = {}
            )
        }
    }
}