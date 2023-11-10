package com.vali.photogallery.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface
import com.vali.photogallery.model.MediaImage
import com.vali.photogallery.model.Query
import com.vali.photogallery.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class ImagesRepositoryImpl(private val contentResolver: ContentResolver) : ImagesRepository {

    override fun getAllImages(): Flow<Result<List<MediaImage>>> {
        val imageList = mutableListOf<MediaImage>()
        val photoQuery = Query.PhotoQuery()
        val query = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            photoQuery.projection,
            photoQuery.bundle,
            null
        )
        query.use { cursor ->
            cursor?.let {
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
                val displayNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME)
                val dateAddedColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_ADDED)
                val bucketDisplayNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val displayName = cursor.getString(displayNameColumn)
                    val dateAdded = cursor.getString(dateAddedColumn)
                    val bucketDisplayName = cursor.getString(bucketDisplayNameColumn)

                    val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                    imageList += MediaImage(
                        contentUri.toString(),
                        displayName ?: "",
                        dateAdded ?: "",
                        bucketDisplayName ?: "",
                        false
                    )
                }
            }
        }
        return flow {
            emit(Result.Success(imageList))
        }
    }

    override fun getExifData(imageUri: Uri): Flow<Map<String, String>> {
        try {
            contentResolver.openInputStream(imageUri).use { inputStream ->
                if (inputStream != null) {
                    val exifInterface = ExifInterface(inputStream)
                    val exifData = hashMapOf<String, String>()
                    val attributes = listOf(
                        ExifInterface.TAG_DATETIME,
                        ExifInterface.TAG_EXPOSURE_TIME,
                        ExifInterface.TAG_FLASH,
                        ExifInterface.TAG_FOCAL_LENGTH,
                        ExifInterface.TAG_IMAGE_LENGTH,
                        ExifInterface.TAG_IMAGE_WIDTH,
                        ExifInterface.TAG_MAKE,
                        ExifInterface.TAG_MODEL,
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.TAG_WHITE_BALANCE
                    )
                    attributes.forEach { tag ->
                        exifInterface.getAttribute(tag)?.let {
                            exifData[tag] = it
                        }
                    }
                    return flow { emit(exifData) }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return flow {
            emit(emptyMap())
        }
    }

    override fun deleteImage(imageUri: Uri): Flow<Result<Unit>> = flow {
        try {
            val deleteUri = MediaStore.setRequireOriginal(imageUri)
            contentResolver.delete(
                deleteUri,
                "${MediaStore.MediaColumns._ID} = ?",
                arrayOf(ContentUris.parseId(imageUri).toString())
            )
            emit(Result.Success(Unit))
        } catch (e: SecurityException) {
            emit(Result.Error(e.message ?: "", exception = e))
        }
    }

}