package com.example.authorization.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authorization.domain.PhotoAuthUseCase
import com.example.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoAuthViewModel @Inject constructor (
    private val authUseCase: PhotoAuthUseCase,
    private val logger: Logger
) : ViewModel() {
    private val _authState = MutableLiveData<String>()
    val authState: LiveData<String> get() = _authState

    fun authenticate(code: String) {
        if (code.isBlank()) return
        viewModelScope.launch {
            try {
                val result = authUseCase.execute(code)
                _authState.postValue(result)
            }
            catch (e:Exception) {
                logger.d("Error in PhotoAuthViewModel",e.toString())
            }
        }
    }

    fun getStoredToken(): String? {
        return authUseCase.getStoredToken()
    }

    override fun onCleared() {
        super.onCleared()
    }
}
