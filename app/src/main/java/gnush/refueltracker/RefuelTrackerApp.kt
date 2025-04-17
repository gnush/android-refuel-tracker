package gnush.refueltracker

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import gnush.refueltracker.ui.navigation.AppNavHost
import gnush.refueltracker.ui.navigation.BottomNavigationDestination
import gnush.refueltracker.ui.navigation.bottomNavBarDestinations

@Composable
fun RefuelTrackerApp(navController: NavHostController = rememberNavController()) {
    AppNavHost(navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    onNavigateUp: (() -> Unit)? = null,
    onSettingsClick: (() -> Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (onNavigateUp != null) {
                IconButton(onClick = onNavigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.nav_back_button_description)
                    )
                }
            }
        },
        actions = {
            if (onSettingsClick != null) {
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = stringResource(R.string.config_button_icon_description)
                    )
                }
            }
        }
    )
}

@Composable
fun CommonBottomAppBar(
    currentDestination: BottomNavigationDestination,
    onNavigationItemClicked: (String) -> Unit,
    floatingActionButton: @Composable (() -> Unit)? = null
) {
    BottomAppBar(
        actions = {
            NavigationBar {
                bottomNavBarDestinations().forEach { destination ->
                    NavigationBarItem(
                        selected = destination == currentDestination,
                        label = { Text(stringResource(destination.labelRes)) },
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = stringResource(destination.iconDescriptionRes)
                            )
                        },
                        onClick = {
                            onNavigationItemClicked(destination.route)
                        }
                    )
                }
            }
        },
        floatingActionButton = floatingActionButton
    )
}