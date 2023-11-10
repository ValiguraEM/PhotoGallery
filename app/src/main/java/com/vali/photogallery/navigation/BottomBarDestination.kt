package com.vali.photogallery.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.vali.photogallery.screens.destinations.AlbumsScreenDestination
import com.vali.photogallery.screens.destinations.AllImagesScreenDestination
import com.vali.photogallery.screens.destinations.FavoritesScreenDestination

enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    val selectedIcon: ImageVector,
    val defaultIcon: ImageVector,
    @StringRes val label: Int
) {
    AllImages(
        direction = AllImagesScreenDestination,
        selectedIcon = Icons.Filled.Home,
        defaultIcon = Icons.Outlined.Home,
        label = com.vali.photogallery.R.string.all_images
    ),
    Albums(
        direction = AlbumsScreenDestination,
        selectedIcon = Icons.Filled.AccountBox,
        defaultIcon = Icons.Outlined.AccountBox,
        label = com.vali.photogallery.R.string.albums
    ),
    Favorites(
        direction = FavoritesScreenDestination,
        selectedIcon = Icons.Filled.Favorite,
        defaultIcon = Icons.Outlined.FavoriteBorder,
        label = com.vali.photogallery.R.string.favorites
    );
}