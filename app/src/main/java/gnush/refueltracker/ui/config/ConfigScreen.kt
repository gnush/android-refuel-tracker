package gnush.refueltracker.ui.config

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
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

// TODO: add to nav graph / onClick topBar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ConfigViewModel = viewModel(factory = RefuelTrackerViewModelProvider.Factory)
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
        Column(modifier = Modifier.padding(innerPadding)) {
            // TODO: Add Sections
            //       - Decimal Places
            //       - Signs
            //       - Drop Down Filter
        }
    }
}

@Composable
private fun SingleInputPreference(
    @StringRes label: Int,
    value: String,
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
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(label),
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.weight(1f))
        TextField(
            value = value,
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
            label = R.string.config_currency_sign_label,
            value = "€",
            onValueChange = {}
        )
    }
}