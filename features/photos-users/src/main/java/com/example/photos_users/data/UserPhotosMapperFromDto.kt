package com.example.photos_users.data

import com.example.photos.api.model.Photo

class UserPhotosMapperFromDto {
    companion object {
        fun mapDtoToModel(responseDto: Photo): UserPhotosModel {
            return UserPhotosModel(
                photoId = responseDto.id,
                photoDescription = responseDto.altDescription,
                pathUrl = responseDto.urls?.small,
                userName = responseDto.user?.username ?: "null",
                name = responseDto.user?.name?:"null"
            )
        }
    }
}
