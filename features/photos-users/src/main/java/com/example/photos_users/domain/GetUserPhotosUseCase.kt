package com.example.photos_users.domain

import com.example.photos_users.data.UserPhotosMapperFromDto
import com.example.photos_users.data.UserPhotosModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetUserPhotosUseCase @Inject constructor(private val repository: UserPhotosRepository) {
    fun execute(username: String?): Flow<List<UserPhotosModel>?> = flow {
        val response = repository.getUserPhotos(username)
        val userPhotos = response?.map { photoDto ->
            UserPhotosMapperFromDto.mapDtoToModel(photoDto)
        }
        emit(userPhotos)
    }.flowOn(Dispatchers.IO)
}
