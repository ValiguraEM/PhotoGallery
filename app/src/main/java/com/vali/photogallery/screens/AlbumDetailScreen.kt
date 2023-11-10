package com.vali.photogallery.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vali.photogallery.vm.GalleryViewModel
import com.vali.photogallery.model.MediaImage
import com.vali.photogallery.screens.actions.ImageAction
import com.vali.photogallery.screens.components.album.AlbumDetailTopBar
import com.vali.photogallery.screens.components.image.ImageList
import com.vali.photogallery.screens.destinations.ImageDetailScreenDestination
import com.vali.photogallery.screens.screenutils.getViewModelStoreOwner

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun AlbumDetailScreen(
    id: String,
    viewModel: GalleryViewModel = hiltViewModel(viewModelStoreOwner = getViewModelStoreOwner()),
    navigator: DestinationsNavigator
) {
    viewModel.selectAlbum(bucketName = id)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            AlbumDetailTopBar(
                title = id,
                onBackClicked = { navigator.navigateUp() }
            )
        }
    ) {
        AlbumDetailScreenContent(
            modifier = Modifier.padding(it),
            albumImages = uiState.imagesForSelectedAlbum,
            onImageClicked = { mediaImage ->
                viewModel.handleImageAction(
                    ImageAction.ShowImageDetail(
                        mediaImage
                    )
                )
                viewModel.handleImageAction(
                    ImageAction.ShowExifData(
                        mediaImage
                    )
                )
                navigator.navigate(
                    ImageDetailScreenDestination()
                )
            },
            onFavoriteToggle = { mediaImage ->
                viewModel.handleImageAction(
                    ImageAction.ToggleFavorite(
                        mediaImage
                    )
                )
            }
        )
    }
}

@Composable
private fun AlbumDetailScreenContent(
    albumImages: List<MediaImage>,
    onImageClicked: (MediaImage) -> Unit,
    onFavoriteToggle: (MediaImage) -> Unit,
    modifier: Modifier
) {
    ImageList(
        modifier = modifier,
        images = albumImages,
        onImageClicked = onImageClicked,
        onFavoriteToggle = onFavoriteToggle
    )
}