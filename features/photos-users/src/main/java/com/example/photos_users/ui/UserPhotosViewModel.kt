package com.example.photos_users.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photos_users.data.UserPhotosModel
import com.example.photos_users.domain.GetUserPhotosUseCase
import com.example.state.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPhotosViewModel @Inject constructor(
    private val userPhotosUseCase: GetUserPhotosUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var page = 1
    var isLoading = false
    var isLastPage = false
    val username = savedStateHandle.get<String>("username")
    var name: String? = savedStateHandle.get<String>("name")

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _postUserPhotos.value = State.Failure(throwable)
        isLoading = false // Сброс флага при ошибке
    }

    private val _postUserPhotos: MutableStateFlow<State<List<UserPhotosModel>>> =
        MutableStateFlow(State.Empty)
    val postUserPhotos: StateFlow<State<List<UserPhotosModel>>>
        get() = _postUserPhotos

    init {
        loadUserPhotos()
    }

     fun loadUserPhotos() {
        if (isLoading || isLastPage) return // Проверка на загрузку или последнюю страницу
        isLoading = true
        viewModelScope.launch(coroutineExceptionHandler) {

            val newPhotos = userPhotosUseCase.execute(username, page)
                .first() //делаем запрос на новые фотографии

            if (newPhotos.isEmpty()) {
                isLastPage = true // Если данные закончились
                _postUserPhotos.value = State.Empty
            } else {
                val currentPhotos =
                    (_postUserPhotos.value as? State.Success)?.data ?: emptyList()
                val updatedPhotos = currentPhotos + newPhotos
                _postUserPhotos.value = State.Success(updatedPhotos)
                isLoading = false
            }
        }
    }

    fun loadNextPage() {
        if (isLoading || isLastPage) return
        page++
        loadUserPhotos()
    }

    public override fun onCleared() {
        super.onCleared()
        _postUserPhotos.value = State.Empty
    }
}
