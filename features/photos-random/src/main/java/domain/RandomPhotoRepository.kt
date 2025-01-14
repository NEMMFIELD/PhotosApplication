package domain

import com.example.photos.api.model.Photo

interface RandomPhotoRepository {
    suspend fun loadRandomPhotos(count: Int): List<Photo>
}
