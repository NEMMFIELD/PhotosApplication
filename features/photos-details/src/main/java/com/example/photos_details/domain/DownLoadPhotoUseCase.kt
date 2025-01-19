package com.example.photos_details.domain

import com.example.photos.api.model.DownloadResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class DownLoadPhotoUseCase @Inject constructor(private val repository: PhotoDetailsRepository) {
    fun execute(id:String): Flow<DownloadResponse> = flow {
        emit(repository.downLoadPhoto(id))
    }.flowOn(Dispatchers.IO)
}
