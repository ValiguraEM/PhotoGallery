package com.vali.photogallery.di

import android.content.ContentResolver
import android.content.Context
import com.vali.photogallery.repository.FavoritesRepository
import com.vali.photogallery.repository.FavoritesRepositoryImpl
import com.vali.photogallery.repository.ImagesRepository
import com.vali.photogallery.repository.ImagesRepositoryImpl
import com.vali.photogallery.database.Database
import com.vali.photogallery.database.FavoritesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }
    @Provides
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }
    @Provides
    fun provideImagesRepository(contentResolver: ContentResolver): ImagesRepository {
        return ImagesRepositoryImpl(contentResolver)
    }
    @Provides
    fun provideFavoritesRepository(dao: FavoritesDao): FavoritesRepository {
        return FavoritesRepositoryImpl(dao)
    }
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): Database {
        return androidx.room.Room.databaseBuilder(
            context,
            Database::class.java,
            "database"
        ).build()
    }

    @Provides
    fun provideFavoriteDao(database: Database) = database.favoritesDao()
}