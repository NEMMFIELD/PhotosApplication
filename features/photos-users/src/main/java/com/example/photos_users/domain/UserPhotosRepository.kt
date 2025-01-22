package com.example.photos_users.domain

import com.example.photos.api.model.Photo


interface UserPhotosRepository {
   suspend fun getUserPhotos(username:String?,page:Int):List<Photo>
}
