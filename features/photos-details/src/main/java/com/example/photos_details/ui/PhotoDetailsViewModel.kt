package com.example.photos_details.ui

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photos_details.datali.PhotoDetailsModel
import com.example.photos_details.domain.DislikePhotoUseCase
import com.example.photos_details.domain.DownLoadPhotoUseCase
import com.example.photos_details.domain.GetAccessTokenUseCase
import com.example.photos_details.domain.LikePhotoUseCase
import com.example.photos_details.domain.PhotoDetailsUseCase
import com.example.state.State
import com.example.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
internal class PhotoDetailsViewModel @Inject constructor(
    private val useCase: PhotoDetailsUseCase,
    private val likePhotoUseCase: LikePhotoUseCase,
    private val dislikePhotoUseCase: DislikePhotoUseCase,
    private val downLoadPhotoUseCase: DownLoadPhotoUseCase,
    private val sharedPreferences: SharedPreferences,
    private val logger: Logger,
    savedStateHandle: SavedStateHandle,
    private val contentResolver: ContentResolver,
) : ViewModel() {
    companion object {
        const val PHOTO_ID = "itemId"
    }

    val token = sharedPreferences.getString("access_token", null)
    val photoId = savedStateHandle.get<String>(PHOTO_ID)
    var username: String? = null
    var name: String? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _postPhoto.value = State.Failure(exception)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val _postPhoto: MutableStateFlow<State<PhotoDetailsModel>> =
        MutableStateFlow(State.Empty)

    val postPhoto: StateFlow<State<PhotoDetailsModel>> = _postPhoto
        .onStart {
            logger.d("Item ID", photoId.toString())
            loadSelectedPhoto(photoId = photoId)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            State.Empty
        )

    private val _downloadedPhoto: MutableStateFlow<State<String>> = MutableStateFlow(State.Empty)
    val downloadedPhoto: StateFlow<State<String>> get() = _downloadedPhoto


    internal fun loadSelectedPhoto(photoId: String?) {
        viewModelScope.launch(exceptionHandler) {
            useCase.execute(photoId ?: "0").collect { photo ->
                _postPhoto.value = State.Success(photo)
                username = photo.userName
                name = photo.name
            }
        }
    }

    fun downLoadPhoto(photoId: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Получаем URL фотографии через UseCase
                val photo = downLoadPhotoUseCase.execute(photoId ?: "0").first()
                val photoUrl = photo.url
                val fileName = "unsplash_photo_${photoId}.jpg"

                // Инициализируем OkHttpClient для загрузки
                val client = OkHttpClient()
                val request = Request.Builder().url(photoUrl).build()
                val response = client.newCall(request).execute()

                if (!response.isSuccessful) throw IOException("Ошибка загрузки: ${response.code}")

                // Получаем поток данных и сохраняем в галерею
                val inputStream = response.body?.byteStream() ?: throw IOException("Пустой ответ")

                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                val uri = contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )
                    ?: throw IOException("Не удалось создать URI для сохранения")

                contentResolver.openOutputStream(uri).use { outputStream ->
                    inputStream.copyTo(outputStream!!)
                }

                inputStream.close()
                _downloadedPhoto.value = State.Success("Фото сохранено в галерею как $fileName")

            } catch (e: Exception) {
                _downloadedPhoto.value = State.Failure(e)
            }
        }
    }


    internal fun toggleLike(photoId: String, accessKey: String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val currentState = _postPhoto.value
            if (currentState is State.Success) {
                val photo = currentState.data
                val isCurrentlyLiked = photo.likedByUser
                val response = if (isCurrentlyLiked == true) {
                    // Выполнить дизлайк
                    dislikePhotoUseCase.execute(photoId).single()
                } else {
                    // Выполнить лайк
                    likePhotoUseCase.execute(photoId).single()
                }
                // Обновить состояние
                val updatedPhoto = photo.copy(
                    likes = response.photo.likes,
                    likedByUser = response.photo.likedByUser
                )
                _postPhoto.value = State.Success(updatedPhoto)
            }
        }
    }
}
