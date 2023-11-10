package com.vali.photogallery.screens.actions

import com.vali.photogallery.model.MediaImage

sealed class ImageAction {
    data class ToggleFavorite(val mediaImage: MediaImage) : ImageAction()
    data class DeleteImage(val mediaImage: MediaImage) : ImageAction()
    data class ShowImageDetail(val mediaImage: MediaImage) : ImageAction()
    data class ShowExifData(val mediaImage: MediaImage) : ImageAction()
}
