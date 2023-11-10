package com.vali.photogallery.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vali.photogallery.vm.GalleryViewModel
import com.vali.photogallery.model.AlbumWithImage
import com.vali.photogallery.screens.components.album.AlbumsGrid
import com.vali.photogallery.screens.destinations.AlbumDetailScreenDestination
import com.vali.photogallery.screens.screenutils.getViewModelStoreOwner

@Destination
@Composable
fun AlbumsScreen(
    viewModel: GalleryViewModel = hiltViewModel(viewModelStoreOwner = getViewModelStoreOwner()),
    navigator: DestinationsNavigator
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AlbumsScreenContent(
        albums = uiState.albums,
        onAlbumClicked = { albumWithImage ->
            navigator.navigate(AlbumDetailScreenDestination(id = albumWithImage.name))
        })
}

@Composable
private fun AlbumsScreenContent(
    albums: List<AlbumWithImage>,
    onAlbumClicked: (AlbumWithImage) -> Unit
) {
    val sortedAlbums = albums.sortedBy { it.name }
    AlbumsGrid(
        albums = sortedAlbums,
        onAlbumClicked = onAlbumClicked
    )
}