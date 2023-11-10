package com.vali.photogallery.repository

import com.vali.photogallery.database.FavoriteImageEntity
import com.vali.photogallery.database.FavoritesDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(private val favoritesDao: FavoritesDao) : FavoritesRepository {
    override suspend fun markAsFavorite(imageId: String) =
        favoritesDao.setFavorite(FavoriteImageEntity(imageId))

    override suspend fun removeFavorite(imageId: String) {
        val favorite = favoritesDao.getFavorite(imageId)
        favorite?.let {
            favoritesDao.removeFavorite(it)
        }
    }

    override fun getAllFavorites(): Flow<List<String>> =
        favoritesDao.getAllFavorites()
            .map { favorites -> favorites.map { it.imageId } }

}