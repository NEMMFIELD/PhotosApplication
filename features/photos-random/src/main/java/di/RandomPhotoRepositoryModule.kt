package di

import com.example.photos.api.PhotosApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import data.RandomPhotoRepositoryImpl
import domain.RandomPhotoRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RandomPhotoRepositoryModule {

    @Provides
    @Singleton
    fun provideRandomPhotoRepository(photosApi: PhotosApi): RandomPhotoRepository =
        RandomPhotoRepositoryImpl(photosApi)
}
