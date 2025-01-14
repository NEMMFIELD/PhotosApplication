package data

import com.example.photos.api.PhotosApi
import com.example.photos.api.model.Photo
import domain.RandomPhotoRepository
import javax.inject.Inject

class RandomPhotoRepositoryImpl @Inject constructor(private val randomPhotosApi: PhotosApi) :
    RandomPhotoRepository {
    override suspend fun loadRandomPhotos(
        count: Int,
    ): List<Photo> {
        return randomPhotosApi.getRandomPhotos(count)
    }
}
