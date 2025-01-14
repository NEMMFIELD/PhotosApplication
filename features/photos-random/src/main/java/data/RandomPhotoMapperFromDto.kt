package data

import com.example.photos.api.model.Photo

class RandomPhotoMapperFromDto {
    companion object {
        fun mapDtoToModel(randomPhotoDto: Photo):RandomPhotoModel {
            return RandomPhotoModel(id = randomPhotoDto.id, description = randomPhotoDto.altDescription, pathUrl = randomPhotoDto.urls?.small)
        }
    }
}
