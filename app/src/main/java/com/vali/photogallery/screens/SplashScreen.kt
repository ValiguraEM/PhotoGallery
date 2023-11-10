package com.vali.photogallery.screens

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vali.photogallery.R
import com.vali.photogallery.vm.GalleryViewModel
import com.vali.photogallery.screens.components.permission.MediaPermissionRequestButton
import com.vali.photogallery.screens.destinations.GalleryScreenDestination
import com.vali.photogallery.screens.screenutils.getViewModelStoreOwner
import kotlinx.coroutines.delay

@OptIn(ExperimentalPermissionsApi::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator,
    viewModel: GalleryViewModel = hiltViewModel(viewModelStoreOwner = getViewModelStoreOwner())
) {
    val imagesPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(permission = Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    Box(contentAlignment = Alignment.Center) {
        Column(
            Modifier
                .padding(16.dp)
                .align(Alignment.Center)) {
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold
            )
            if (imagesPermissionState.status.isGranted) {
                LaunchedEffect(imagesPermissionState.status.isGranted) {
                    viewModel.loadInitialData()
                    delay(2000)
                    viewModel.navigationEvent.collect {
                        navigator.navigate(GalleryScreenDestination())
                    }
                }
            } else {
                MediaPermissionRequestButton {}
            }
        }
    }
}



