package com.example.photos.search.data

import com.example.photos.api.PhotosApi
import com.example.photos.api.model.SearchPhotosResponse
import com.example.photos.search.domain.SearchPhotosRepository
import javax.inject.Inject

class SearchPhotosRepositoryImpl @Inject constructor(private val api: PhotosApi) :
    SearchPhotosRepository {
    override suspend fun searchPhotos(query: String, page: Int): SearchPhotosResponse {
        return api.searchPhotos(query, page)
    }
}
