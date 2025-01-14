package com.example.authorization.di

import android.content.Context
import android.content.SharedPreferences
import com.example.authorization.data.PhotoAuthRepositoryImpl
import com.example.authorization.domain.PhotoAuthRepository
import com.example.photos.api.PhotosApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthRepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        api: PhotosApi,
        @ApplicationContext context: Context,
        sharedPreferences: SharedPreferences
    ): PhotoAuthRepository {
        return PhotoAuthRepositoryImpl(api, sharedPreferences)
    }
}
