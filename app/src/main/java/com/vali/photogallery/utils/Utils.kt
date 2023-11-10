package com.vali.photogallery.utils

import com.vali.photogallery.model.MediaImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun groupImagesByDateAdded(images: List<MediaImage>): Map<String, List<MediaImage>> {
    val dateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
    val groupedImages = images.groupBy {
        val timestampInMillis = it.dateAdded.toLong() * 1000
        dateFormat.format(Date(timestampInMillis))
    }
    return groupedImages
}