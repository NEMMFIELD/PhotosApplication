package com.example.photos.search.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photos.search.data.PhotoModel
import com.example.photos.search.domain.SearchPhotosUseCase
import com.example.state.State
import com.example.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchPhotosViewModel @Inject constructor(
    private val searchPhotosUseCase: SearchPhotosUseCase,
    private val savedStateHandle: SavedStateHandle,
    logger: Logger
) : ViewModel() {

    private val queryFlow = MutableStateFlow("")
    var page: Int = 1
    var isLoading = false
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _allPhotos.value = State.Failure(throwable)
    }

    //Все фотографии, включая пагинацию
    private val _allPhotos: MutableStateFlow<State<List<PhotoModel>>> =
        MutableStateFlow(State.Empty)
    val allPhotos: StateFlow<State<List<PhotoModel>>> get() = _allPhotos


    init {
        viewModelScope.launch(coroutineExceptionHandler) {
            queryFlow
                .debounce(200)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    if (query.isNotEmpty()) {
                        page = 1
                        _allPhotos.value = State.Empty
                        logger.d("Search", "Method executed")
                        searchPhotosUseCase.execute(query, page)
                    } else {
                        flowOf(emptyList())
                    }
                }.onStart { _allPhotos.value = State.Empty }
                .catch { throwable ->
                    _allPhotos.value = State.Failure(throwable)
                }
                .collect { searchingPhotos ->
                    _allPhotos.value = State.Success(searchingPhotos)
                }
        }
    }

    fun searchPhotos(query: String) {
        queryFlow.value = query.trim()
    }

    fun loadMorePhotos() {
        if (isLoading || queryFlow.value.isEmpty()) return
        isLoading = true
        viewModelScope.launch(coroutineExceptionHandler + Dispatchers.IO) {
            val newPhotos = searchPhotosUseCase.execute(queryFlow.value, ++page).first()
            if (newPhotos.isNotEmpty()) {
                val currentPhotos = (_allPhotos.value as? State.Success)?.data ?: emptyList()
                val updatedPhotos = currentPhotos + newPhotos
                _allPhotos.value = State.Success(updatedPhotos)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _allPhotos.value = State.Empty
    }
}
