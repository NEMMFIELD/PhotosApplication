package com.example.photos_details.di

import com.example.photos.api.PhotosApi
import com.example.photos_details.data.PhotoDetailsRepositoryImpl
import com.example.photos_details.domain.PhotoDetailsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class PhotoRepositoryModule {
    @Provides
    @Singleton
    fun providePhotoRepository(photosApi: PhotosApi):PhotoDetailsRepository = PhotoDetailsRepositoryImpl(photosApi)
}
