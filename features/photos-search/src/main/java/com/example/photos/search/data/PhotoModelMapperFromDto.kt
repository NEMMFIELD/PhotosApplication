package com.example.photos.search.data

import com.example.photos.api.model.Photo

class PhotoModelMapperFromDto {
    companion object {
        fun mapDtoToModel(searchPhotoDto: Photo):PhotoModel {
            return PhotoModel(id = searchPhotoDto.id, description = searchPhotoDto.altDescription, pathUrl = searchPhotoDto.urls?.small)
        }
    }
}
