package com.example.photos_random.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photos_random.BuildConfig
import com.example.state.State
import dagger.hilt.android.lifecycle.HiltViewModel
import data.RandomPhotoModel
import domain.RandomPhotoUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val RANDOM_PHOTO_COUNT = 20
@HiltViewModel
class RandomPhotoViewModel @Inject constructor(private val useCase: RandomPhotoUseCase) :
    ViewModel() {
    var isDataLoaded = false

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _postRandomPhoto.value = State.Failure(throwable)
    }

    private val _postRandomPhoto: MutableStateFlow<State<List<RandomPhotoModel>>> =
        MutableStateFlow(State.Empty)
    val postRandomPhoto: StateFlow<State<List<RandomPhotoModel>>> get()  = _postRandomPhoto
        .onStart { loadRandomPhoto( RANDOM_PHOTO_COUNT ) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            State.Empty
        )

     fun loadRandomPhoto( count: Int) {
        if (isDataLoaded) return
        viewModelScope.launch(coroutineExceptionHandler) {
            withContext(Dispatchers.IO) {
                useCase.execute( count,).collect { randomPhotos ->
                    _postRandomPhoto.value = State.Success(randomPhotos)
                    isDataLoaded = true
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        isDataLoaded = false
    }
}
