package io.github.gnush.refueltracker

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.vector.ImageVector
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
    extraActions: List<TopAppBarAction> = emptyList(),
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
                    onAboutClick = onAboutClick,
                    extraActions = extraActions
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

data class TopAppBarAction(
    @StringRes val text: Int,
    val onClick: () -> Unit,
    val icon: ImageVector,
    @StringRes val iconDescription: Int
)

@Composable
fun CommonTopAppBarActions(
    modifier: Modifier = Modifier,
    onSettingsClick: (() -> Unit)? = null,
    onAboutClick: (() -> Unit)? = null,
    extraActions: List<TopAppBarAction> = emptyList()
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier.padding(dimensionResource(R.dimen.padding_tiny))
    ) {
        IconButton(
            onClick = { expanded = !expanded }
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.menu_button_icon_description)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            extraActions.forEach {
                DropdownMenuItem(
                    text = { Text(stringResource(it.text)) },
                    onClick = it.onClick,
                    leadingIcon = {
                        Icon(
                            imageVector = it.icon,
                            contentDescription = stringResource(it.iconDescription)
                        )
                    }
                )
            }

            if (extraActions.isNotEmpty() && (onSettingsClick != null || onAboutClick != null))
                HorizontalDivider()

            if (onSettingsClick != null)
                SettingsDropDownMenuItem(onSettingsClick)

            if (onAboutClick != null)
                AboutDropDownMenuItem(onAboutClick)
        }
    }
}

@Composable
private fun AboutDropDownMenuItem(onCLick: () -> Unit) {
    DropdownMenuItem(
        text = { Text(stringResource(R.string.about_screen)) },
        onClick = onCLick,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = stringResource(R.string.about_info_icon_description)
            )
        }
    )
}

@Composable
private fun SettingsDropDownMenuItem(onCLick: () -> Unit) {
    DropdownMenuItem(
        text = { Text(stringResource(R.string.settings_screen)) },
        onClick = onCLick,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = stringResource(R.string.settings_button_icon_description)
            )
        }
    )
}