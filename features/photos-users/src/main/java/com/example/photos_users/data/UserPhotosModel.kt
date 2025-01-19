package com.example.photos_users.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserPhotosModel(
    val photoId: String?,
    val photoDescription: String?,
    val pathUrl: String?,
    val userName: String,
    val name:String
) : Parcelable
