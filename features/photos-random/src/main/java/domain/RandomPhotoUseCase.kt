package domain

import data.RandomPhotoMapperFromDto
import data.RandomPhotoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RandomPhotoUseCase @Inject constructor(private val randomPhotoRepository: RandomPhotoRepository) {
    fun execute(count: Int): Flow<List<RandomPhotoModel>> =
        flow {
            val modelFromDto = randomPhotoRepository.loadRandomPhotos(count)
            val randomPhotos = modelFromDto.map { modelFromDto ->
                RandomPhotoMapperFromDto.mapDtoToModel(modelFromDto)
            }
            emit(randomPhotos)
        }.flowOn(Dispatchers.IO)
}
