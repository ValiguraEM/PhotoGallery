package com.vali.photogallery.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setFavorite(favorite: FavoriteImageEntity)

    @Query("SELECT * FROM favorites WHERE id = :imageId")
    suspend fun getFavorite(imageId: String): FavoriteImageEntity?

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<FavoriteImageEntity>>

    @Delete
    suspend fun removeFavorite(favorite: FavoriteImageEntity)
}