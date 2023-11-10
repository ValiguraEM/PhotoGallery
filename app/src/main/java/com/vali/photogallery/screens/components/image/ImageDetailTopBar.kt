package com.vali.photogallery.screens.components.image

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.vali.photogallery.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDetailTopBar(
    title: String,
    onBackClicked: () -> Unit,
    onAddToFavoritesClicked: () -> Unit,
    onInfoClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    isFavorite: Boolean
) {
    TopAppBar(
        title = { Text(text = title, maxLines = 1) },
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.back)
                )
            }
        },
        actions = {
            IconButton(onClick = onAddToFavoritesClicked) {
                Icon(
                    imageVector = if (isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = stringResource(R.string.add_to_or_remove_from_favorites)
                )
            }
            IconButton(onClick = onInfoClicked) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = stringResource(R.string.info)
                )
            }
            IconButton(onClick = onDeleteClicked) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = stringResource(R.string.delete)
                )
            }
        }
    )
}
