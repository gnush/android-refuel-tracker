package org.refueltracker.ui.fuelstop

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.refueltracker.CommonTopAppBar
import org.refueltracker.R
import org.refueltracker.ui.navigation.NavigationDestination
import org.refueltracker.ui.theme.RefuelTrackerTheme

object FuelStopEditDestination: NavigationDestination {
    override val route = "fuel_stop_edit"
    override val titleRes: Int = R.string.fuel_stop_edit_screen
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelStopEditScreen(
    onNavigateUp: () -> Unit,
    onSaveClickNavigateTo: () -> Unit,
    modifier: Modifier = Modifier,
    canNavigateUp: Boolean = true
) {
//    Scaffold(
//        topBar = {
//            CommonTopAppBar(
//                title = stringResource(FuelStopEditDestination.titleRes),
//                canNavigateUp = canNavigateUp,
//                onNavigateUp = onNavigateUp
//            )
//        },
//        modifier = modifier
//    ) { innerPadding ->
//        FuelStopEntryBody(
//            onSaveClick = onSaveClickNavigateTo,
//            modifier = Modifier
//                .padding(innerPadding)
//                .verticalScroll(rememberScrollState())
//                .fillMaxWidth()
//        )
//    }
}

@Preview(showBackground = true)
@Composable
private fun FuelStopEditScreenPreview() {
    RefuelTrackerTheme {
        FuelStopEditScreen(
            onNavigateUp = {},
            onSaveClickNavigateTo = {}
        )
    }
}