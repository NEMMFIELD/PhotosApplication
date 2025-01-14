package com.example.photos.api.model

import com.squareup.moshi.Json
import kotlinx.serialization.Serializable

@Serializable
class PhotoLikesCountResponse(
    @Json(name = "likes")
    val likes: Int
)
