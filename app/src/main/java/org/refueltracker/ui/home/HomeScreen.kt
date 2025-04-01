package org.refueltracker.ui.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.refueltracker.R
import org.refueltracker.ui.navigation.NavigationDestination

object HomeDestination: NavigationDestination {
    override val route: String = "home"
    @StringRes override val titleRes: Int = R.string.app_name
}

@Composable
fun HomeScreen() {
    Scaffold(

    ) { innerPadding ->
        Text(text = "nothing to see here", modifier = Modifier.padding(innerPadding))
    }
}