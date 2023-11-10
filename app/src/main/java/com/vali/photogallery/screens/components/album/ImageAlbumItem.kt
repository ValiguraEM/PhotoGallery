package com.vali.photogallery.screens.components.album

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.vali.photogallery.R
import com.vali.photogallery.model.AlbumWithImage

@Composable
fun ImageAlbum(modifier: Modifier, albumWithImage: AlbumWithImage, onAlbumClicked: (AlbumWithImage) -> Unit) {
    val uri = albumWithImage.image.id.toUri()
    Column(modifier.padding(8.dp)) {
        Text(
            text = albumWithImage.name,
            modifier = Modifier.padding(4.dp),
            fontSize = 20.sp
        )
        AsyncImage(
            model = uri,
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(id = R.string.image),
            modifier = Modifier
                .size(512.dp, 512.dp)
                .clickable { onAlbumClicked(albumWithImage) },
        )
    }
}