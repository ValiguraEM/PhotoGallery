package com.vali.photogallery.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteImageEntity::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
}