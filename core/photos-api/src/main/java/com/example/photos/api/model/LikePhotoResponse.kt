package com.example.photos.api.model


import kotlinx.serialization.Serializable

@Serializable
class LikePhotoResponse(
    val photo: Photo,
    val user: User
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LikePhotoResponse) return false
        return photo == other.photo && user == other.user
    }

    override fun hashCode(): Int {
        var result = photo.hashCode()
        result = 31 * result + user.hashCode()
        return result
    }
}
