package com.vali.photogallery.screens.states

import com.vali.photogallery.model.AlbumWithImage
import com.vali.photogallery.model.MediaImage

data class GalleryState(
    val allImages: List<MediaImage> = emptyList(),
    val albums: List<AlbumWithImage> = emptyList(),
    val favorites: List<MediaImage> = emptyList(),
    val imagesForSelectedAlbum: List<MediaImage> = emptyList(),
    val exifData: Map<String, String> = emptyMap(),
    val imageDeletionState: DeletionState = DeletionState()
)

data class GalleryStateBuilder (
    var allImages: List<MediaImage>? = null,
    var albums: List<AlbumWithImage>? = null,
    var favorites: List<MediaImage>? = null,
    var imagesForSelectedAlbum: List<MediaImage>? = null,
    var exifData: Map<String, String>? = null,
    var imageDeletionState: DeletionState? = null
) {
    fun build(currentState: GalleryState): GalleryState {
            return GalleryState(
                allImages = allImages ?: currentState.allImages,
                albums = albums ?: currentState.albums,
                favorites = favorites ?: currentState.favorites,
                imagesForSelectedAlbum = imagesForSelectedAlbum ?: currentState.imagesForSelectedAlbum,
                exifData = exifData ?: currentState.exifData,
                imageDeletionState = imageDeletionState ?: currentState.imageDeletionState
            )

    }
}
