package com.example.photos.search.domain

import com.example.photos.api.model.Photo
import com.example.photos.api.model.SearchPhotosResponse

interface SearchPhotosRepository {
    suspend fun searchPhotos(query:String,page:Int): SearchPhotosResponse
}
