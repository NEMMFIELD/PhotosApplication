package com.example.photos.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
data class DownloadResponse (
    @Json(name="url")
    val url:String
)
