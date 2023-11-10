package com.vali.photogallery.screens

import android.app.Activity
import android.app.RecoverableSecurityException
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vali.photogallery.R
import com.vali.photogallery.model.MediaImage
import com.vali.photogallery.screens.actions.ImageAction
import com.vali.photogallery.screens.components.dialogs.DeleteConfirmationDialog
import com.vali.photogallery.screens.components.dialogs.ExifInfoDialog
import com.vali.photogallery.screens.components.image.ImageDetailTopBar
import com.vali.photogallery.vm.GalleryViewModel
import com.vali.photogallery.screens.components.image.TransformableImage
import com.vali.photogallery.screens.screenutils.getViewModelStoreOwner
import com.vali.photogallery.screens.states.DeletionState

@Destination
@Composable
fun ImageDetailScreen(
    viewModel: GalleryViewModel = hiltViewModel(viewModelStoreOwner = getViewModelStoreOwner()),
    navigator: DestinationsNavigator
) {
    val currentImage by viewModel.currentImage.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    currentImage?.let {
        ImageDetailScreenContent(currentImage = it,
            deletionState = uiState.imageDeletionState,
            exifData = uiState.exifData,
            onAddToFavoritesClicked = { mediaImage ->
                viewModel.handleImageAction(
                    ImageAction.ToggleFavorite(
                        mediaImage
                    )
                )
            },
            onRequestDeleteImage = { mediaImage ->
                viewModel.handleImageAction(
                    action = ImageAction.DeleteImage(
                        mediaImage
                    )
                )
            },
            onResetImageDeletionState = viewModel::resetDeletionState,
            onNavigateUp = { navigator.navigateUp() })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDetailScreenContent(
    currentImage: MediaImage,
    deletionState: DeletionState,
    exifData: Map<String, String>,
    onAddToFavoritesClicked: (MediaImage) -> Unit,
    onRequestDeleteImage: (MediaImage) -> Unit,
    onResetImageDeletionState: () -> Unit,
    onNavigateUp: () -> Unit

) {
    val showDialog = remember { mutableStateOf(false) }
    val showDeleteConfirmDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val deleteRequestLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            onRequestDeleteImage(currentImage)
        } else {
            Toast.makeText(
                context, R.string.image_cannot_be_deleted_toast, Toast.LENGTH_LONG
            ).show()
        }
    }

    if (showDeleteConfirmDialog.value) {
        DeleteConfirmationDialog(onDismiss = { showDeleteConfirmDialog.value = false }) {
            showDeleteConfirmDialog.value = false
            onRequestDeleteImage(currentImage)
        }
    }

    if (deletionState.deletionError is RecoverableSecurityException) {
        onResetImageDeletionState()
        val recoverableSecurityException = deletionState.deletionError
        val intentSender = recoverableSecurityException.userAction.actionIntent.intentSender
        val intentSenderRequest = IntentSenderRequest.Builder(intentSender).build()
        deleteRequestLauncher.launch(intentSenderRequest)
    }

    Scaffold(topBar = {
        ImageDetailTopBar(title = currentImage.displayName,
            onBackClicked = onNavigateUp,
            onAddToFavoritesClicked = {
                onAddToFavoritesClicked(currentImage)
            },
            onInfoClicked = { showDialog.value = true },
            isFavorite = currentImage.isFavorite,
            onDeleteClicked = { showDeleteConfirmDialog.value = true })
    }, content = {
        Box(modifier = Modifier.padding(it)) {
            TransformableImage(uri = currentImage.id)
        }

    })

    LaunchedEffect(key1 = deletionState.isDeleted) {
        if (deletionState.isDeleted == true) {
            Toast.makeText(context, R.string.image_deleted_toast, Toast.LENGTH_SHORT).show()
            onNavigateUp()
            onResetImageDeletionState()
        }
    }

    if (showDialog.value) {
        ExifInfoDialog(exifData = exifData, onDismiss = { showDialog.value = false })
    }
}