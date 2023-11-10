package com.vali.photogallery.screens.components.image

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.vali.photogallery.R
import com.vali.photogallery.model.MediaImage

@Composable
fun GalleryImageItem(
    mediaImage: MediaImage,
    onClicked: (MediaImage) -> Unit,
    onFavoriteToggle: (MediaImage) -> Unit,
    modifier: Modifier = Modifier
) {
    val uri = mediaImage.id.toUri()

    Box(modifier = modifier) {
        AsyncImage(
            model = uri,
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(id = R.string.image),
            modifier = modifier
                .clickable {
                    onClicked(mediaImage)
                }
        )

        Icon(
            imageVector = if (mediaImage.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = stringResource(R.string.favorite_icon),
            tint = if (mediaImage.isFavorite) Color.Red else Color.Black,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(24.dp)
                .clickable {
                    onFavoriteToggle(mediaImage)
                }
        )
    }
}

