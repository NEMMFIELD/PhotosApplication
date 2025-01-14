package com.example.photosapplication.di

import com.example.utils.AndroidLogger
import com.example.utils.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoggerModule {
    @Provides
    @Singleton
    fun provideLogger(): Logger {
        return AndroidLogger()
    }
}
