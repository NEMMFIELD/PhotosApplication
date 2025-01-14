package com.example.photos_details.ui

import android.content.SharedPreferences
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photos_details.datali.PhotoDetailsModel
import com.example.photos_details.domain.DislikePhotoUseCase
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
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PhotoDetailsViewModel @Inject constructor(
    private val useCase: PhotoDetailsUseCase,
    private val likePhotoUseCase: LikePhotoUseCase,
    private val dislikePhotoUseCase: DislikePhotoUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val sharedPreferences: SharedPreferences,
    private val logger: Logger,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    companion object {
        const val PHOTO_ID = "itemId"
    }

    val token = sharedPreferences.getString("access_token", null)
    val photoId = savedStateHandle.get<String>(PHOTO_ID)

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


    internal fun loadSelectedPhoto(photoId: String?) {
        viewModelScope.launch(exceptionHandler) {
            useCase.execute(photoId ?: "0").collect { photo ->
                _postPhoto.value = State.Success(photo)
            }
        }
    }


     internal suspend fun getAccessKey(clientId: String, secretKey: String, code: String): String =
        getAccessTokenUseCase.execute(clientId, secretKey, code)

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
