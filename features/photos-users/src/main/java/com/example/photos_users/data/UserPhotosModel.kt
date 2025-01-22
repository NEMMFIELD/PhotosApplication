package com.example.photos_users.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
 data class UserPhotosModel(
    val photoId: String?,
    val photoDescription: String?,
    val pathUrl: String?,
    val userName: String,
    val name:String
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserPhotosModel) return false

        return photoId == other.photoId &&
                pathUrl == other.pathUrl &&
                photoDescription == other.photoDescription
    }

    override fun hashCode(): Int {
        return photoId.hashCode() * 31 +
                pathUrl.hashCode() * 31 +
                photoDescription.hashCode()
    }
}
