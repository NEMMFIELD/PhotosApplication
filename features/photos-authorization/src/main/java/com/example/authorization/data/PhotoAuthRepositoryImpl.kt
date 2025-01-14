package com.example.authorization.data

import android.content.SharedPreferences
import com.example.authorization.domain.PhotoAuthRepository
import com.example.photos.api.PhotosApi
import com.example.photos_random.BuildConfig
import javax.inject.Inject

class PhotoAuthRepositoryImpl @Inject constructor(
    private val api: PhotosApi,
    private val sharedPreferences: SharedPreferences
) : PhotoAuthRepository {
    override suspend fun getAccessToken(code: String): String {
        val response = api.getAccessToken(
            clientId = BuildConfig.ACCESS_KEY,
            clientSecret = BuildConfig.SECRET_KEY,
            redirectUri = "urn:ietf:wg:oauth:2.0:oob",
            authorizationCode = code
        )
        saveToken(response.accessToken)
        return response.accessToken
    }

    override fun saveToken(token: String) {
        sharedPreferences.edit().putString("access_token", token).apply()
    }

    override fun getToken(): String? = sharedPreferences.getString("access_token", null)
}
