package io.github.gnush.refueltracker.ui.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
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
import io.github.gnush.refueltracker.ui.Displayable
import io.github.gnush.refueltracker.ui.DropDownSelection
import io.github.gnush.refueltracker.ui.RefuelTrackerViewModelProvider
import io.github.gnush.refueltracker.ui.createNumberFormat
import io.github.gnush.refueltracker.ui.data.CustomDateFormat
import io.github.gnush.refueltracker.ui.data.DateFormat
import io.github.gnush.refueltracker.ui.navigation.NavigationDestination
import io.github.gnush.refueltracker.ui.theme.RefuelTrackerTheme
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import java.util.Calendar

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
        SettingsScreenBody(
            uiState = uiState,
            paddingValues = innerPadding,
            onDateFormatSelected = viewModel::saveDateFormat,
            onDateFormatPatternChange = viewModel::saveDateFormatPattern,
            onSeparateLargeNumbersToggle = viewModel::saveSeparateLargeNumbers,
            onLargeNumbersSeparatorPlacesChange = viewModel::saveLargeNumbersSeparatorPlaces,
            onCurrencyDecimalPlacesChange = viewModel::saveCurrencyDecimalPlaces,
            onVolumeDecimalPlacesChange = viewModel::saveVolumeDecimalPlaces,
            onCurrencyVolumeRatioDecimalPlacesChange = viewModel::saveRatioDecimalPlaces,
            onCurrencySignChange = viewModel::saveCurrencySign,
            onVolumeSignChange = viewModel::saveVolumeSign,
            onNumberOfDropDownElementsChange = viewModel::saveNumberOfDropDownElements,
            onDropDownFilterSelected = viewModel::saveInitialDropDownFilter,
            dateFormats = viewModel.dateFormats,
            entryScreenDropDownFilterItems = viewModel.entryScreenDropDownFilterItems
        )
    }
}

@Composable
private fun SettingsScreenBody(
    uiState: SettingsUiState,
    onDateFormatSelected: (DateFormat) -> Unit,
    onDateFormatPatternChange: (String) -> Unit,
    onSeparateLargeNumbersToggle: (Boolean) -> Unit,
    onLargeNumbersSeparatorPlacesChange: (String) -> Unit,
    onCurrencyDecimalPlacesChange: (String) -> Unit,
    onVolumeDecimalPlacesChange: (String) -> Unit,
    onCurrencyVolumeRatioDecimalPlacesChange: (String) -> Unit,
    onCurrencySignChange: (String) -> Unit,
    onVolumeSignChange: (String) -> Unit,
    onNumberOfDropDownElementsChange: (String) -> Unit,
    onDropDownFilterSelected: (DropDownSelection) -> Unit,
    dateFormats: List<DateFormat>,
    entryScreenDropDownFilterItems: List<DropDownSelection>,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = modifier
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        SettingsCategory(title = R.string.settings_category_date_formats_title) {
            FormattedDate(
                label = R.string.settings_example_date_display_label,
                format = uiState.dateFormat.get
            )
            ReadOnlyDropDownPreference(
                label = R.string.settings_date_format_drop_down_label,
                currentSelected = uiState.dateFormat,
                onItemSelected = onDateFormatSelected,
                items = dateFormats
            )
            if (uiState.dateFormat is CustomDateFormat)
                SingleInputPreference(
                    label = R.string.settings_date_format_pattern_label,
                    preference = uiState.dateFormatPattern,
                    onValueChange = onDateFormatPatternChange
                )
        }
        SettingsCategoryDivider()
        SettingsCategory(title = R.string.settings_category_number_formats_title) {
            FormattedNumber(
                label = R.string.settings_example_number_display_label,
                separateLargeNumbers = uiState.separateLargeNumbers,
                largeNumberSeparator = uiState.largeNumberSeparator
            )
            SwitchPreference(
                label = R.string.settings_use_large_number_separator_label,
                value = uiState.separateLargeNumbers,
                onValueChange = onSeparateLargeNumbersToggle
            )
            if (uiState.separateLargeNumbers)
                SingleInputPreference(
                    label = R.string.settings_large_number_separator_places_label,
                    preference = uiState.largeNumberSeparator,
                    onValueChange = onLargeNumbersSeparatorPlacesChange,
                    hasNumericKeyboard = true
                )
            SettingsSubcategoryHeader(R.string.settings_sub_category_decimal_places_headline)
            SingleInputPreference(
                label = R.string.settings_currency_decimal_places_label,
                preference = uiState.currencyDecimalPlaces,
                onValueChange = onCurrencyDecimalPlacesChange,
                hasNumericKeyboard = true
            )
            FormattedNumber(
                label = R.string.settings_example_number_display_label,
                separateLargeNumbers = uiState.separateLargeNumbers,
                largeNumberSeparator = uiState.largeNumberSeparator,
                decimalPlaces = uiState.currencyDecimalPlaces
            )
            SingleInputPreference(
                label = R.string.settings_volume_decimal_places_label,
                preference = uiState.volumeDecimalPlaces,
                onValueChange = onVolumeDecimalPlacesChange,
                hasNumericKeyboard = true
            )
            FormattedNumber(
                label = R.string.settings_example_number_display_label,
                separateLargeNumbers = uiState.separateLargeNumbers,
                largeNumberSeparator = uiState.largeNumberSeparator,
                decimalPlaces = uiState.volumeDecimalPlaces
            )
            SingleInputPreference(
                label = R.string.settings_ratio_decimal_places_label,
                preference = uiState.ratioDecimalPlaces,
                onValueChange = onCurrencyVolumeRatioDecimalPlacesChange,
                hasNumericKeyboard = true
            )
            FormattedNumber(
                label = R.string.settings_example_number_display_label,
                separateLargeNumbers = uiState.separateLargeNumbers,
                largeNumberSeparator = uiState.largeNumberSeparator,
                decimalPlaces = uiState.ratioDecimalPlaces
            )
        }
        SettingsCategoryDivider()
        SettingsCategory(title = R.string.settings_category_symbols_title) {
            SingleInputPreference(
                label = R.string.settings_currency_sign_label,
                preference = Preference(uiState.currencySign, true),
                onValueChange = onCurrencySignChange
            )
            SingleInputPreference(
                label = R.string.settings_volume_sign_label,
                preference = Preference(uiState.volumeSign, true),
                onValueChange = onVolumeSignChange
            )
        }
        SettingsCategoryDivider()
        SettingsCategory(
            title = R.string.settings_category_drop_down_title
        ) {
            SingleInputPreference(
                label = R.string.settings_num_drop_down_elements_label,
                preference = uiState.numDropDownElements,
                onValueChange = onNumberOfDropDownElementsChange
            )
            ReadOnlyDropDownPreference(
                label = R.string.settings_default_drop_down_filter_label,
                currentSelected = uiState.defaultDropDownFilter,
                onItemSelected = onDropDownFilterSelected,
                items = entryScreenDropDownFilterItems
            )
        }
    }
}

@Composable
private fun SettingsCategory(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.headlineMedium
        )
        content()
    }
}

@Composable
private fun SettingsSubcategoryHeader(
    @StringRes headline: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(headline),
        style = MaterialTheme.typography.headlineSmall,
        modifier = modifier
    )
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
private fun FormattedDate(
    @StringRes label: Int,
    format: DateTimeFormat<LocalDate>,
    modifier: Modifier = Modifier
) {
    CenteredPreferenceRow(modifier) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = LocalDate(
                year = Calendar.getInstance().get(Calendar.YEAR),
                monthNumber = Calendar.getInstance().get(Calendar.MONTH)+1,
                dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
            ).format(format),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun FormattedNumber(
    @StringRes label: Int,
    separateLargeNumbers: Boolean,
    largeNumberSeparator: Preference,
    modifier: Modifier = Modifier,
    decimalPlaces: Preference? = null
) {
    CenteredPreferenceRow(modifier = modifier) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(Modifier.weight(1f))
        if (largeNumberSeparator.isValid && decimalPlaces == null) {
            val separateAfter = try {
                largeNumberSeparator.value.toInt()
            } catch (_: NumberFormatException) {
                0
            }

            Text(
                text = createNumberFormat(
                    separateLargeNumbers,
                    separateAfter
                ).format(1234567),
                style = MaterialTheme.typography.labelMedium
            )
        } else if (largeNumberSeparator.isValid && decimalPlaces != null && decimalPlaces.isValid) {
            val separateAfter = try {
                largeNumberSeparator.value.toInt()
            } catch (_: NumberFormatException) {
                0
            }
            val decimalPlacesValue = try {
                decimalPlaces.value.toInt()
            } catch (_: NumberFormatException) {
                0
            }

            Text(
                text = createNumberFormat(
                    separateLargeNumbers,
                    separateAfter,
                    decimalPlacesValue
                ).format(0.0000000),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T : Displayable> ReadOnlyDropDownPreference(
    @StringRes label: Int,
    currentSelected: T,
    onItemSelected: (T) -> Unit,
    items: List<T>,
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
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                modifier = Modifier
                    .width(dimensionResource(R.dimen.settings_text_field_width))
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach {
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
private fun SwitchPreferencePreview() {
    RefuelTrackerTheme {
        SwitchPreference(
            label = R.string.settings_use_large_number_separator_label,
            value = true,
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
                preference = Preference(value = "€", isValid = true),
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

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    RefuelTrackerTheme {
        SettingsScreenBody(
            uiState = SettingsUiState(),
            onDateFormatSelected = {},
            onDateFormatPatternChange = {},
            onSeparateLargeNumbersToggle = {},
            onLargeNumbersSeparatorPlacesChange = {},
            onCurrencyDecimalPlacesChange = {},
            onVolumeDecimalPlacesChange = {},
            onCurrencyVolumeRatioDecimalPlacesChange = {},
            onCurrencySignChange = {},
            onVolumeSignChange = {},
            onNumberOfDropDownElementsChange = {},
            onDropDownFilterSelected = {},
            dateFormats = emptyList(),
            entryScreenDropDownFilterItems = emptyList()
        )
    }
}