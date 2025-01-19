package com.example.photos_details.data

import com.example.photos.api.model.Photo
import com.example.photos_details.datali.PhotoDetailsModel

class PhotoDetailsMapperFromDto {
    companion object {
        fun mapDtoToDetailsModel(randomPhotoDto: Photo): PhotoDetailsModel {
            return PhotoDetailsModel(
                id = randomPhotoDto.id,
                description = randomPhotoDto.altDescription ?: "No description",
                pathUrl = randomPhotoDto.urls?.regular,
                downloads = randomPhotoDto.downloads,
                likes = randomPhotoDto.likes,
                likedByUser = randomPhotoDto.likedByUser,
                userPortfolio = randomPhotoDto.user?.portfolioUrl ?: "No portfolio",
                location = randomPhotoDto.user?.location ?: "No location",
                userName = randomPhotoDto.user?.username,
                name = randomPhotoDto.user?.name
            )
        }
    }
}
