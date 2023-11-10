package com.vali.photogallery.repository

import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    suspend fun markAsFavorite(imageId: String)

    suspend fun removeFavorite(imageId: String)

    fun getAllFavorites(): Flow<List<String>>
}