package io.github.gnush.refueltracker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.github.gnush.refueltracker.ui.navigation.AppNavHost
import io.github.gnush.refueltracker.ui.navigation.BottomNavigationDestination
import io.github.gnush.refueltracker.ui.navigation.bottomNavBarDestinations

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
    onAboutClick: (() -> Unit)? = null,
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
            if (onSettingsClick != null || onAboutClick != null)
                CommonTopAppBarActions(
                    onSettingsClick = onSettingsClick,
                    onAboutClick = onAboutClick
                )
        }
    )
}

@Composable
fun CommonBottomAppBar(
    currentDestination: BottomNavigationDestination,
    onNavigationItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    floatingActionButton: @Composable (() -> Unit)? = null
) {
    BottomAppBar(
        modifier = modifier,
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

@Composable
fun CommonTopAppBarActions(
    modifier: Modifier = Modifier,
    onSettingsClick: (() -> Unit)? = null,
    onAboutClick: (() -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier.padding(dimensionResource(R.dimen.padding_tiny))
    ) {
        IconButton(
            onClick = { expanded = !expanded }
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = stringResource(R.string.menu_button_icon_description)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (onSettingsClick != null)
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.settings_screen)) },
                    onClick = onSettingsClick,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.settings_button_icon_description)
                        )
                    }
                )

            if (onAboutClick != null)
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.about_screen)) },
                    onClick = onAboutClick,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.about_info_icon_description)
                        )
                    }
                )
        }
    }
}