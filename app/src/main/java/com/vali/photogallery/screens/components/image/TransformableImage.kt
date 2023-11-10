package com.vali.photogallery.screens.components.image

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import coil.compose.AsyncImage
import com.vali.photogallery.R

@Composable
fun TransformableImage(uri: String) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    var imageCoordinates: LayoutCoordinates? = null
    val maxScale = 5f

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { centroid, panChange, zoomChange, _ ->
                    imageCoordinates?.let { coordinates ->
                        val localCentroid = coordinates.windowToLocal(centroid)
                        if (localCentroid.x in 0f..coordinates.size.width.toFloat() &&
                            localCentroid.y in 0f..coordinates.size.height.toFloat()
                        ) {
                            val newScale = (scale * zoomChange).coerceIn(1f, maxScale)
                            val imageBounds = coordinates.size.toSize()
                            val containerBounds = containerSize.toSize()
                            val correctedOffset = (offset + panChange / scale * newScale)
                            val maxX = maxOf(
                                (imageBounds.width * newScale - containerBounds.width) / 2,
                                0f
                            )
                            val maxY = maxOf(
                                (imageBounds.height * newScale - containerBounds.height) / 2,
                                0f
                            )
                            offset = Offset(
                                x = correctedOffset.x.coerceIn(-maxX, maxX),
                                y = correctedOffset.y.coerceIn(-maxY, maxY)
                            )
                            scale = newScale
                        }
                    }
                }
            }
    ) {
        AsyncImage(
            model = uri,
            contentScale = ContentScale.Fit,
            contentDescription = stringResource(id = R.string.image),
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .onGloballyPositioned { coordinates ->
                    imageCoordinates = coordinates
                    if (containerSize == IntSize.Zero) {
                        containerSize = coordinates.size
                    }
                }
        )
    }
}