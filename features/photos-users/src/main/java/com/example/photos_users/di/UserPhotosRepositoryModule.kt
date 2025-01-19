package com.example.photos_users.di

import com.example.photos.api.PhotosApi
import com.example.photos_users.data.UserPhotosRepositoryImpl
import com.example.photos_users.domain.UserPhotosRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserPhotosRepositoryModule {
    @Provides
    @Singleton
    fun provideRandomPhotoRepository(photosApi: PhotosApi): UserPhotosRepository =
        UserPhotosRepositoryImpl(photosApi)
}
