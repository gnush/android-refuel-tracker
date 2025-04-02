package org.refueltracker.ui.fuelstop

import android.icu.util.Currency
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import org.refueltracker.CommonTopAppBar
import org.refueltracker.R
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
    canNavigateUp: Boolean = true
) {
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
            onSaveClick = onSaveClickNavigateTo,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
private fun FuelStopEntryBody(
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_large)),
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        FuelStopInputForm()
        Button(
            onClick = onSaveClick,
            enabled = true, // TODO: validate input
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_button))
        }
    }
}

@Composable
private fun FuelStopInputForm(
    modifier: Modifier = Modifier,
    inputEnabled: Boolean = true
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        modifier = modifier
    ) {
        val colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
        )
        val modifier = Modifier.fillMaxWidth()
        val decimalKeyboard = KeyboardOptions(keyboardType = KeyboardType.Decimal)

        // TODO: state
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text(stringResource(R.string.fuel_stop_station_form_label)) },
            colors = colors,
            modifier = modifier,
            enabled = inputEnabled,
            singleLine = true
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text(stringResource(R.string.fuel_stop_price_per_volume_form_label)) },
            colors = colors,
            modifier = modifier,
            enabled = inputEnabled,
            singleLine = true,
            keyboardOptions = decimalKeyboard,
            trailingIcon = {
                Row {
                    Text(Currency.getInstance(java.util.Locale.getDefault()).symbol)
                    Text("/L") // TODO: configuration on display currency and volume
                }
            }
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text(stringResource(R.string.fuel_stop_total_volume_form_label)) },
            colors = colors,
            modifier = modifier,
            enabled = inputEnabled,
            singleLine = true,
            keyboardOptions = decimalKeyboard,
            trailingIcon = { Text("L") }
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text(stringResource(R.string.fuel_stop_total_paid_form_label)) },
            colors = colors,
            modifier = modifier,
            enabled = inputEnabled,
            singleLine = true,
            keyboardOptions = decimalKeyboard,
            leadingIcon = { Text(Currency.getInstance(java.util.Locale.getDefault()).symbol) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FuelStopEntryPreview() {
    RefuelTrackerTheme {
        FuelStopEntryBody(
            onSaveClick = {}
        )
    }
}