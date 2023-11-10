package com.vali.photogallery.screens.components.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vali.photogallery.model.MediaImage

@Composable
fun ImageList(
    modifier: Modifier,
    images: List<MediaImage>,
    onImageClicked: (MediaImage) -> Unit,
    onFavoriteToggle: (MediaImage) -> Unit,
) {
    if (images.isEmpty()) {
        EmptyList(modifier)
    } else {
        ImageListLayout(
            images = images,
            onImageClicked = { mediaImage -> onImageClicked(mediaImage) },
            onFavoriteToggle = onFavoriteToggle
        )
    }
}