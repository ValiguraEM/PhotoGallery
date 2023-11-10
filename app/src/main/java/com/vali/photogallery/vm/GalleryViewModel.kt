package com.vali.photogallery.vm

import android.app.RecoverableSecurityException
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vali.photogallery.screens.states.DeletionState
import com.vali.photogallery.repository.FavoritesRepository
import com.vali.photogallery.screens.states.GalleryState
import com.vali.photogallery.screens.actions.ImageAction
import com.vali.photogallery.repository.ImagesRepository
import com.vali.photogallery.model.AlbumWithImage
import com.vali.photogallery.model.MediaImage
import com.vali.photogallery.model.Result
import com.vali.photogallery.screens.states.GalleryStateBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val imagesRepository: ImagesRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GalleryState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = Channel<Unit>(Channel.CONFLATED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    private val _currentImage = MutableStateFlow<MediaImage?>(null)
    val currentImage = _currentImage.asStateFlow()

    fun loadInitialData() {
        getAllImages()
        getAllFavorites()
    }

    fun handleImageAction(action: ImageAction) {
        when (action) {
            is ImageAction.ToggleFavorite -> handleFavoriteToggle(action.mediaImage)
            is ImageAction.DeleteImage -> requestDeleteImage(action.mediaImage)
            is ImageAction.ShowImageDetail -> getCurrentImage(action.mediaImage.id)
            is ImageAction.ShowExifData -> getExifData(action.mediaImage.id)
        }
    }

    fun selectAlbum(bucketName: String) {
        viewModelScope.launch {
            val imagesForAlbum =
                _uiState.value.allImages.filter { it.bucketDisplayName == bucketName }
            updateUiState {
                imagesForSelectedAlbum = imagesForAlbum
            }
        }
    }

    fun resetDeletionState() {
        viewModelScope.launch(Dispatchers.Main) {
            updateUiState {
                imageDeletionState = DeletionState()
            }
        }
    }

    private fun getCurrentImage(imageId: String) {
        viewModelScope.launch {
            _uiState.value.allImages.find { it.id == imageId }?.let {
                _currentImage.value = it
            }
        }
    }

    private fun handleFavoriteToggle(image: MediaImage) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedImage = withContext(Dispatchers.Default) {
                val isFavorite = image.isFavorite
                val updatedImage = image.copy(isFavorite = !isFavorite)
                if (isFavorite) {
                    favoritesRepository.removeFavorite(image.id)
                } else {
                    favoritesRepository.markAsFavorite(image.id)
                }
                updatedImage
            }

            withContext(Dispatchers.Main) {
                val uiStateValue = _uiState.value
                val updatedAllImages =
                    updateImageList(uiStateValue.allImages, image.id, updatedImage)
                val updatedFavorites =
                    updateImageList(uiStateValue.favorites, image.id, updatedImage)
                val updatedImagesForSelectedAlbum =
                    updateImageList(uiStateValue.imagesForSelectedAlbum, image.id, updatedImage)

                _currentImage.update { updatedImage }

                updateUiState {
                    allImages = updatedAllImages
                    favorites = updatedFavorites
                    imagesForSelectedAlbum = updatedImagesForSelectedAlbum
                }
            }
        }
    }

    private fun updateImageList(
        images: List<MediaImage>,
        imageId: String,
        updatedImage: MediaImage
    ): List<MediaImage> {
        val index = images.indexOfFirst { it.id == imageId }
        if (index != -1) {
            return images.toMutableList().apply {
                set(index, updatedImage)
            }
        }
        return images
    }

    private fun getExifData(imageId: String) {
        viewModelScope.launch {
            imagesRepository.getExifData(imageId.toUri()).collect { exifData ->
                updateUiState {
                    this.exifData = exifData
                }
            }
        }
    }

    private fun requestDeleteImage(mediaImage: MediaImage) {
        val imageUri = mediaImage.id.toUri()
        viewModelScope.launch {
            try {
                imagesRepository.deleteImage(imageUri).collect { resource ->
                    when (resource) {
                        is Result.Success -> {
                            updateUiState {
                                imageDeletionState = DeletionState(isDeleted = true)
                            }
                            removeImageFromGallery(imageUri)
                            favoritesRepository.removeFavorite(imageUri.toString())
                        }

                        is Result.Error -> {
                            updateUiState {
                                imageDeletionState =
                                    DeletionState(deletionError = resource.exception)

                            }
                        }
                    }
                }
            } catch (e: SecurityException) {
                (e as? RecoverableSecurityException)?.let {
                    updateUiState {
                        imageDeletionState = DeletionState(isDeleted = false)
                    }
                } ?: throw e
            }
        }
    }

    private fun removeImageFromGallery(uri: Uri) {
        viewModelScope.launch(Dispatchers.Main) {
            val newAllImagesList = _uiState.value.allImages.filterNot { it.id.toUri() == uri }
            val newFavoritesList = _uiState.value.favorites.filterNot { it.id.toUri() == uri }
            val newAlbums = generateAlbumsWithImages(newAllImagesList)
            updateUiState {
                allImages = newAllImagesList
                favorites = newFavoritesList
                albums = newAlbums
            }
        }
    }

    private fun updateUiState(builder: GalleryStateBuilder.() -> Unit) {
        _uiState.update { currentState ->
            GalleryStateBuilder().apply {
                builder()
            }.build(currentState)
        }
    }

    private fun getAllImages() {
        viewModelScope.launch(Dispatchers.IO) {
            imagesRepository.getAllImages().collect { res ->
                val sortedImages = res.data?.sortedByDescending { it.dateAdded } ?: emptyList()
                withContext(Dispatchers.Main) {
                    updateUiState {
                        allImages = sortedImages
                        albums = generateAlbumsWithImages(sortedImages)
                    }
                }
            }
        }
    }

    private fun generateAlbumsWithImages(images: List<MediaImage>): List<AlbumWithImage> {
        val albumMap = images.groupBy { it.bucketDisplayName }
        return albumMap.map { (bucketDisplayName, imagesInAlbum) ->
            AlbumWithImage(bucketDisplayName, imagesInAlbum.first())
        }
    }

    private fun getAllFavorites() {
        viewModelScope.launch {
            favoritesRepository.getAllFavorites().combine(_uiState) { favoritesIds, uiState ->
                val updatedImages = uiState.allImages.map { image ->
                    image.copy(isFavorite = image.id in favoritesIds)
                }
                updatedImages to updatedImages.filter { it.isFavorite }
            }.collect { (updatedImages, favoriteImages) ->
                updateUiState {
                    allImages = updatedImages
                    favorites = favoriteImages
                }
                _navigationEvent.send(Unit)
            }
        }
    }
}