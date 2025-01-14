package com.example.photos_details.domain

import com.example.photos_details.data.PhotoDetailsMapperFromDto
import com.example.photos_details.datali.PhotoDetailsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class PhotoDetailsUseCase @Inject constructor(private val repository: PhotoDetailsRepository) {
    fun execute(id: String): Flow<PhotoDetailsModel> = flow {
        val data = repository.loadPhoto(id)
        val photoDetails = PhotoDetailsMapperFromDto.mapDtoToDetailsModel(data)
        emit(photoDetails)
    }.flowOn(Dispatchers.IO)
}
