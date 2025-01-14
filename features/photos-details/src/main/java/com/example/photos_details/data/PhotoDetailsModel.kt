package com.example.photos_details.datali

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class PhotoDetailsModel(
    val id: String?,
    val description: String?,
    val pathUrl: String?,
    val downloads: Int?,
    val likes: Int?,
    val likedByUser: Boolean?,
    val userPortfolio: @RawValue Any? = "",
    val location: @RawValue Any? = null
) : Parcelable

