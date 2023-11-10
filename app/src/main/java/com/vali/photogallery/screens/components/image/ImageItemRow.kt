package com.vali.photogallery.screens.components.image

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vali.photogallery.model.MediaImage

@Composable
fun ImageItemRow(
    rowImages: List<MediaImage>,
    onImageClicked: (MediaImage) -> Unit,
    onFavoriteToggle: (MediaImage) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        rowImages.forEach { image ->
            GalleryImageItem(
                mediaImage = image,
                onClicked = onImageClicked,
                onFavoriteToggle = onFavoriteToggle,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            )
        }

        repeat(3 - rowImages.size) {
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            )
        }
    }
}