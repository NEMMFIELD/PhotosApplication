package com.example.photos_details.domain

import com.example.photos.api.model.LikePhotoResponse
import com.example.photos.api.model.Photo

internal interface PhotoDetailsRepository {
    suspend fun loadPhoto(id: String): Photo
    suspend fun getAccessToken(clientId: String, clientSecret: String, code: String): String
    suspend fun likePhoto(id: String, ): LikePhotoResponse
    suspend fun dislikePhoto(id: String,): LikePhotoResponse
}
