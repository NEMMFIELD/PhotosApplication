package com.example.photos.search.domain

import com.example.photos.search.data.PhotoModel
import com.example.photos.search.data.PhotoModelMapperFromDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchPhotosUseCase @Inject constructor(private val repository: SearchPhotosRepository) {
    fun execute(query: String, page: Int): Flow<List<PhotoModel>> = flow {
        if (query.isNotEmpty()) {
            val responseFromApi = repository.searchPhotos(query, page)
            val mappedList = responseFromApi.results.map { photoDto ->
                PhotoModelMapperFromDto.mapDtoToModel(photoDto)
            }
            emit(mappedList)
        } else emit(emptyList())

    }.flowOn(Dispatchers.IO)
}
