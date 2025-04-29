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
            if (extraActions.isNotEmpty() || onSettingsClick != null || onAboutClick != null)
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

@Composable
fun CommonTopAppBarActions(
    modifier: Modifier = Modifier,
    onSettingsClick: (() -> Unit)? = null,
    onAboutClick: (() -> Unit)? = null,
    extraActions: List<TopAppBarAction> = emptyList()
) {
    Box(
        modifier.padding(dimensionResource(R.dimen.padding_tiny))
    ) {
        when {
            extraActions.size == 1 && onSettingsClick == null && onAboutClick == null ->
                TopAppBarActionIconButton(extraActions.first())
            extraActions.isEmpty() && onSettingsClick != null && onAboutClick == null ->
                TopAppBarActionIconButton(settingsAction(onSettingsClick))
            extraActions.isEmpty() && onSettingsClick == null && onAboutClick != null ->
                TopAppBarActionIconButton(aboutAction(onAboutClick))
            else -> {
                var expanded by remember { mutableStateOf(false) }

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
                        TopAppBarActionDropDownItem(it)
                    }

                    if (extraActions.isNotEmpty() && (onSettingsClick != null || onAboutClick != null))
                        HorizontalDivider()

                    if (onSettingsClick != null)
                        TopAppBarActionDropDownItem(settingsAction(onSettingsClick))

                    if (onAboutClick != null)
                        TopAppBarActionDropDownItem(aboutAction(onAboutClick))
                }
            }
        }
    }
}

@Composable
private fun TopAppBarActionDropDownItem(action: TopAppBarAction) {
    DropdownMenuItem(
        text = { Text(stringResource(action.text)) },
        onClick = action.onClick,
        leadingIcon = {
            Icon(
                imageVector = action.icon,
                contentDescription = stringResource(action.iconDescription)
            )
        }
    )
}

@Composable
private fun TopAppBarActionIconButton(action: TopAppBarAction) {
    IconButton(
        action.onClick
    ) {
        Icon(
            imageVector = action.icon,
            contentDescription = stringResource(action.iconDescription)
        )
    }
}

data class TopAppBarAction(
    @StringRes val text: Int,
    val onClick: () -> Unit,
    val icon: ImageVector,
    @StringRes val iconDescription: Int
)

private fun settingsAction(onClick: () -> Unit): TopAppBarAction = TopAppBarAction(
    text = R.string.settings_screen,
    icon = Icons.Default.Settings,
    iconDescription = R.string.settings_button_icon_description,
    onClick = onClick
)

private fun aboutAction(onClick: () -> Unit): TopAppBarAction = TopAppBarAction(
    text = R.string.about_screen,
    icon = Icons.Outlined.Info,
    iconDescription = R.string.about_info_icon_description,
    onClick = onClick
)