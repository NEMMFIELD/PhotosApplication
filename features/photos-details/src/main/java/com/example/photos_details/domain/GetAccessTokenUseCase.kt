package com.example.photos_details.domain

import javax.inject.Inject

internal class GetAccessTokenUseCase @Inject constructor(private val repository: PhotoDetailsRepository) {
    suspend fun execute(clientId: String, clientSecret:String,code:String): String {
      return repository.getAccessToken(clientId,clientSecret,code)
    }
}
