package com.example.photos_details.datali

import android.os.Parcelable
import com.example.photos.api.model.User
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
    val location: @RawValue Any? = null,
    val userName:String? = null, //переменная никнейма пользователя
    val name:String? = null //переменная имени пользователя
) : Parcelable

