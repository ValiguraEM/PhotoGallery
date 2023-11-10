package com.vali.photogallery.screens.components.bottom_navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.ramcosta.composedestinations.navigation.navigate
import com.vali.photogallery.navigation.BottomBarDestination
import com.vali.photogallery.screens.NavGraphs
import com.vali.photogallery.screens.appCurrentDestinationAsState
import com.vali.photogallery.screens.destinations.Destination
import com.vali.photogallery.screens.startAppDestination

@Composable
fun BottomBar(navController: NavController) {
    val bottomBarDestinations = remember { BottomBarDestination.values() }
    val currentDestination: Destination = navController.appCurrentDestinationAsState().value
        ?: NavGraphs.root.startAppDestination

    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.background,
    ) {
        bottomBarDestinations.forEach { destination ->
            val selected = destination.direction == currentDestination
            BottomNavigationItem(
                selectedContentColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    navController.navigate(destination.direction) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                selected = currentDestination == destination.direction,
                icon = {
                    if (selected) {
                        Icon(
                            destination.selectedIcon,
                            contentDescription = stringResource(destination.label)
                        )
                    } else {
                        Icon(
                            destination.defaultIcon,
                            contentDescription = stringResource(destination.label)
                        )
                    }
                },
                label = {
                    Text(
                        stringResource(id = destination.label),
                        fontWeight = if (selected) FontWeight.Bold else null
                    )
                })
        }
    }
}