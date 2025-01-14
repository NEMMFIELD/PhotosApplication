package com.example.authorization.domain

import javax.inject.Inject

class PhotoAuthUseCase @Inject constructor(private val repository: PhotoAuthRepository){
   suspend fun execute(code:String):String {
        return repository.getAccessToken(code)
    }

    fun getStoredToken(): String? {
        return repository.getToken()
    }
}
