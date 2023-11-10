package com.vali.photogallery.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vali.photogallery.model.MediaImage
import com.vali.photogallery.screens.components.image.EmptyList
import com.vali.photogallery.screens.components.image.GalleryImageItem

@Composable
fun FavoritesList(
    images: List<MediaImage>,
    onImageClicked: (MediaImage) -> Unit,
    onFavoriteToggle: (MediaImage) -> Unit
) {
    if (images.isEmpty()) {
        EmptyList(modifier = Modifier)
    } else {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(200.dp),
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            content = {
                items(images) { image ->
                    GalleryImageItem(
                        mediaImage = image,
                        onClicked = onImageClicked,
                        onFavoriteToggle = onFavoriteToggle
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}