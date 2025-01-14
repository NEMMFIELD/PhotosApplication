package com.example.photos.search.di

import com.example.photos.api.PhotosApi
import com.example.photos.search.data.SearchPhotosRepositoryImpl
import com.example.photos.search.domain.SearchPhotosRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SearchPhotoRepositoryModule {
    @Provides
    @Singleton
    fun provideSearchPhotosRepository(photosApi: PhotosApi): SearchPhotosRepository =
        SearchPhotosRepositoryImpl(photosApi)
}
