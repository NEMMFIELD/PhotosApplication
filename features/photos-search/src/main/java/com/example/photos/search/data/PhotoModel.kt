package com.example.photos.search.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoModel(val id:String? ,val description:String? ,val pathUrl:String?):Parcelable
