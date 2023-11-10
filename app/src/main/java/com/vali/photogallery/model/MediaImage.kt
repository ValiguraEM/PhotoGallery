package com.vali.photogallery.model

import android.content.ContentResolver
import android.os.Bundle
import android.provider.MediaStore

data class MediaImage(
    val id: String,
    val displayName: String,
    val dateAdded: String,
    val bucketDisplayName: String,
    val isFavorite: Boolean
)

sealed class Query(
    var projection: Array<String>,
    var bundle: Bundle? = null
) {
    class PhotoQuery : Query(
        projection = arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATE_ADDED,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME
        ),
        bundle = defaultBundle.apply {
            putString(
                ContentResolver.QUERY_ARG_SQL_SELECTION,
                MediaStore.MediaColumns.MIME_TYPE + " like ?"
            )
            putStringArray(
                ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS,
                arrayOf("image%")
            )
        }
    )

    companion object {
        val defaultBundle = Bundle().apply {
            putStringArray(
                ContentResolver.QUERY_ARG_SORT_COLUMNS,
                arrayOf(MediaStore.MediaColumns.DATE_MODIFIED)
            )
            putInt(
                ContentResolver.QUERY_ARG_SQL_SORT_ORDER,
                ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
            )
        }
    }
}

