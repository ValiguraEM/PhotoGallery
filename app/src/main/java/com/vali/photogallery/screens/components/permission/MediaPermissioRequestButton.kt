package com.vali.photogallery.screens.components.permission

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vali.photogallery.R

@Composable
fun MediaPermissionRequestButton(
    onPermissionGranted: () -> Unit
) {
    val readImagesPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                onPermissionGranted()
            } else {
                //TODO
            }
        }
    )
    Text(text = stringResource(R.string.permission_rationale))
    Button(modifier = Modifier.wrapContentSize().padding(top = 16.dp), onClick = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            readImagesPermissionResultLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            readImagesPermissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }) {
        Text(text = stringResource(R.string.request_image_permission))
    }
}