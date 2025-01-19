package com.example.photos_users.data


import com.example.photos.api.PhotosApi
import com.example.photos.api.model.Photo
import com.example.photos_users.domain.UserPhotosRepository
import javax.inject.Inject

class UserPhotosRepositoryImpl @Inject constructor(private val api: PhotosApi):UserPhotosRepository {
    override suspend fun getUserPhotos(username: String?): List<Photo>? {
        return username?.let { api.getUserPhotos(it) }
    }
}
