package com.example.photos_details.domain

import com.example.photos.api.model.LikePhotoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class LikePhotoUseCase @Inject constructor(private val repository: PhotoDetailsRepository) {
    fun execute(photoId: String, ): Flow<LikePhotoResponse> = flow {
        val response = repository.likePhoto(photoId)
        emit(response)
    }.flowOn(Dispatchers.IO)
}
