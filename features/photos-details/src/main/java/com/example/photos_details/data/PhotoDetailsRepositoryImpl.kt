package com.example.photos_details.data
import com.example.photos.api.PhotosApi
import com.example.photos.api.model.LikePhotoResponse
import com.example.photos.api.model.Photo
import com.example.photos_details.domain.PhotoDetailsRepository
import javax.inject.Inject

internal class PhotoDetailsRepositoryImpl @Inject constructor(private val api: PhotosApi) :
    PhotoDetailsRepository {
    override suspend fun loadPhoto(id: String): Photo {
        return api.getPhoto(id)
    }

    override suspend fun getAccessToken(clientId: String, clientSecret:String,code:String): String {
            val response = api.getAccessToken(
                clientId = clientId,
                clientSecret = clientSecret,
                redirectUri = "urn:ietf:wg:oauth:2.0:oob",
                grantType = "authorization_code",
                authorizationCode = code
            )
           return response.accessToken
    }

    override suspend fun likePhoto(id: String): LikePhotoResponse {
        val response = api.likePhoto(id,)
        return response
    }

    override suspend fun dislikePhoto(id: String): LikePhotoResponse {
        val response = api.dislikePhoto(id)
        return response
    }
}
