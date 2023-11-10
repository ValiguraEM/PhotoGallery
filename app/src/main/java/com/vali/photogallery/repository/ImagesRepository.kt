package com.vali.photogallery.repository

import android.net.Uri
import com.vali.photogallery.model.MediaImage
import com.vali.photogallery.model.Result
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {
    fun getAllImages(): Flow<Result<List<MediaImage>>>

    fun deleteImage(imageUri: Uri): Flow<Result<Unit>>

    fun getExifData(imageUri: Uri): Flow<Map<String, String>>
}