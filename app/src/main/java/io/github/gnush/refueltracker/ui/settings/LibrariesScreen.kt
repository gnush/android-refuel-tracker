package io.github.gnush.refueltracker.ui.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import io.github.gnush.refueltracker.CommonTopAppBar
import io.github.gnush.refueltracker.R
import io.github.gnush.refueltracker.ui.navigation.NavigationDestination

object LibrariesDestination: NavigationDestination {
    override val route: String = "libraries"
    @StringRes override val titleRes: Int = R.string.libs_screen
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibrariesScreen(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier,
        topBar = {
            CommonTopAppBar(
                title = stringResource(LibrariesDestination.titleRes),
                onNavigateUp = onNavigateUp,
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        LibrariesContainer(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        )
    }
}