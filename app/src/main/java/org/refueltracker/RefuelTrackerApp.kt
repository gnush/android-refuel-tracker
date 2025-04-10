package org.refueltracker

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.refueltracker.ui.navigation.AppNavHost
import org.refueltracker.ui.navigation.BottomNavigationDestination
import org.refueltracker.ui.navigation.bottomNavBarDestinations

@Composable
fun RefuelTrackerApp(navController: NavHostController = rememberNavController()) {
    AppNavHost(navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopAppBar(
    title: String,
    canNavigateUp: Boolean,
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateUp) {
                IconButton(onClick = onNavigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.nav_back_button_description)
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
                            //navBarSelectedItem = bottomNavBarDestinations().indexOf(destination)
                            onNavigationItemClicked(destination.route)
                        }
                    )
                }
            }
        },
        floatingActionButton = floatingActionButton
    )
}