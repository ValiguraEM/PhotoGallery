package com.vali.photogallery.screens.components.image

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.vali.photogallery.model.MediaImage
import com.vali.photogallery.utils.groupImagesByDateAdded

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageListLayout(
    images: List<MediaImage>,
    onImageClicked: (MediaImage) -> Unit,
    onFavoriteToggle: (MediaImage) -> Unit,
) {
    val gridItems by remember(images) { mutableStateOf(groupImagesByDateAdded(images)) }
    val sections = gridItems.keys

    LazyColumn {
        sections.forEach { section ->
            stickyHeader {
                StickyHeaderItem(section = section)
            }

            val chunkedImages = gridItems[section]?.chunked(3) ?: listOf()

            items(chunkedImages) { rowImages ->
                ImageItemRow(
                    rowImages = rowImages,
                    onImageClicked = onImageClicked,
                    onFavoriteToggle = onFavoriteToggle
                )
            }
        }
    }
}


