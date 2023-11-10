package com.vali.photogallery.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteImageEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val imageId: String
)
