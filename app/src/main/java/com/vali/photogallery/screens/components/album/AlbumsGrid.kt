package com.vali.photogallery.screens.components.album

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vali.photogallery.model.AlbumWithImage
import com.vali.photogallery.screens.components.image.EmptyList

@Composable
fun AlbumsGrid(albums: List<AlbumWithImage>, onAlbumClicked: (AlbumWithImage) -> Unit) {
    if (albums.isEmpty()) {
      EmptyList(modifier = Modifier)
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(albums) { albumWithImage ->
                ImageAlbum(
                    modifier = Modifier.aspectRatio(1f),
                    albumWithImage = albumWithImage,
                    onAlbumClicked = onAlbumClicked
                )
            }
        }
    }
}