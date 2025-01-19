package com.example.photos_users.ui

import android.util.Log
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPhotosViewModel @Inject constructor(
    private val userPhotosUseCase: GetUserPhotosUseCase,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val username = savedStateHandle.get<String>("username")
    var name:String? = savedStateHandle.get<String>("name")

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _postUserPhotos.value = State.Failure(throwable)
    }

    private val _postUserPhotos: MutableStateFlow<State<List<UserPhotosModel>?>> =
        MutableStateFlow(State.Empty)
    val postUserPhotos: StateFlow<State<List<UserPhotosModel>?>>
        get()  = _postUserPhotos
        .onStart { loadUserPhotos(username) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            State.Empty
        )

    private fun loadUserPhotos(username:String?) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            userPhotosUseCase.execute(username).collect { userPhoto ->
                _postUserPhotos.value = State.Success(userPhoto)
                Log.d("username received:",username.toString())
                Log.d("name received:",name.toString())
            }
        }
    }
}
