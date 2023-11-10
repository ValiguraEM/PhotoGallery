package com.vali.photogallery.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.vali.photogallery.navigation.BottomBarDestination
import com.vali.photogallery.screens.components.bottom_navigation.BottomBar
import com.vali.photogallery.screens.destinations.AllImagesScreenDestination
import com.vali.photogallery.vm.GalleryViewModel

@com.ramcosta.composedestinations.annotation.Destination
@Composable
fun GalleryScreen() {
    val navController = rememberNavController()
    GalleryContent(navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryContent(navController: NavHostController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val shouldShowBottomBar = remember(currentRoute) {
        BottomBarDestination.values().any { it.direction.route == currentRoute }
    }
    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomBar(navController = navController)
            }
        },
    ) {
        DestinationsNavHost(
            navGraph = NavGraphs.root,
            navController = navController,
            startRoute = AllImagesScreenDestination,
            modifier = Modifier.padding(it),
            dependenciesContainerBuilder = {
                dependency(NavGraphs.root) {
                    hiltViewModel<GalleryViewModel>()
                }
            }
        )
    }
}