package data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RandomPhotoModel(val id:String? ,val description:String? ,val pathUrl:String?):Parcelable
