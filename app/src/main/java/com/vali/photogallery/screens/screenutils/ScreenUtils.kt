package com.vali.photogallery.screens.screenutils

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun getViewModelStoreOwner(): ComponentActivity {
    return LocalContext.current as ComponentActivity
}