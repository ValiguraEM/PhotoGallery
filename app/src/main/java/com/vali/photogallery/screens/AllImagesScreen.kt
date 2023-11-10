package com.vali.photogallery.screens

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
import com.vali.photogallery.screens.components.image.ImageList
import com.vali.photogallery.screens.destinations.ImageDetailScreenDestination
import com.vali.photogallery.screens.screenutils.getViewModelStoreOwner

@Destination
@Composable
fun AllImagesScreen(
    viewModel: GalleryViewModel = hiltViewModel(viewModelStoreOwner = getViewModelStoreOwner()),
    navigator: DestinationsNavigator
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AllImagesScreenContent(
        modifier = Modifier,
        images = uiState.allImages,
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

@Composable
fun AllImagesScreenContent(
    modifier: Modifier,
    images: List<MediaImage>,
    onImageClicked: (MediaImage) -> Unit,
    onFavoriteToggle: (MediaImage) -> Unit
) {
    ImageList(
        modifier = modifier,
        images = images,
        onImageClicked = onImageClicked,
        onFavoriteToggle = onFavoriteToggle
    )
}

