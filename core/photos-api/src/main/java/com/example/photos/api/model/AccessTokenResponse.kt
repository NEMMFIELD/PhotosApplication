package com.example.photos.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
data class AccessTokenResponse(
    @Json(name="access_token")
    val accessToken: String,
)
