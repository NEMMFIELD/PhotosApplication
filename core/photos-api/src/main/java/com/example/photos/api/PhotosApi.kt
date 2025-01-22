package com.example.photos.api


import com.example.photos.api.model.AccessTokenResponse
import com.example.photos.api.model.DownloadResponse
import com.example.photos.api.model.LikePhotoResponse
import com.example.photos.api.model.Photo
import com.example.photos.api.model.SearchPhotosResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface PhotosApi {
    //Authorization
    @POST("https://unsplash.com/oauth/token")
    suspend fun getAccessToken(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("redirect_uri") redirectUri: String,
        @Query("code") authorizationCode: String,
        @Query("grant_type") grantType: String = "authorization_code"
    ): AccessTokenResponse

    //Fetch photos
    @GET("photos/random")
    suspend fun getRandomPhotos(
        @Query("count") count: Int,
        @Header("Cache-Control") cacheControl: String = "public, max-age=60",
    ): List<Photo>

    @GET("photos/{id}")
    suspend fun getPhoto(@Path("id") id: String): Photo

    @GET("photos/{id}/download")
    suspend fun downloadPhoto(@Path("id") id: String): DownloadResponse

    //fetch user's photos
    @GET("users/{username}/photos")
    suspend fun getUserPhotos(
        @Path("username") username: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10
    ): List<Photo>


    //like or dislike photos
    @POST("photos/{id}/like")
    suspend fun likePhoto(
        @Path("id") id: String,
    ): LikePhotoResponse

    @DELETE("photos/{id}/like")
    suspend fun dislikePhoto(
        @Path("id") id: String,
    ): LikePhotoResponse

    //search photos by query
    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10,
        @Header("Cache-Control") cacheControl: String = "public, max-age=60",
    ): SearchPhotosResponse
}
