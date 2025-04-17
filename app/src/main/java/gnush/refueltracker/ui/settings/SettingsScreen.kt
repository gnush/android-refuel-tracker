package gnush.refueltracker.ui.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import gnush.refueltracker.CommonTopAppBar
import gnush.refueltracker.R
import gnush.refueltracker.ui.RefuelTrackerViewModelProvider
import gnush.refueltracker.ui.navigation.NavigationDestination
import gnush.refueltracker.ui.theme.RefuelTrackerTheme

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
            modifier = Modifier.padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // TODO: date/time formats encoding: enum/sealed class Predefined values or by pattern
            //       add composable for drop down choice
//            SettingsCategory(
//                title = R.string.settings_category_date_formats_title
//            )
//            SettingsCategoryDivider()
            SettingsCategory(title = R.string.settings_category_number_formats_title) {
                SingleInputPreference(
                    label = R.string.settings_large_number_separator_places_label,
                    preference = uiState.thousandsSeparator,
                    onValueChange = viewModel::saveThousandsSeparator,
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
                // TODO: add binary choice toggle option (left/right each one value) for default filter
                //       new composable function
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

    Row(
        modifier = modifier
            .padding(
                start = dimensionResource(R.dimen.padding_medium),
                end = dimensionResource(R.dimen.padding_small),
                top = dimensionResource(R.dimen.padding_tiny),
                bottom = dimensionResource(R.dimen.padding_tiny)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
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
            modifier = Modifier.width(dimensionResource(R.dimen.config_sign_text_field_width))
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