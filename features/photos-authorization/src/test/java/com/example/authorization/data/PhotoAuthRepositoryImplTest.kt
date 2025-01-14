package com.example.authorization.data

import android.content.SharedPreferences
import com.example.photos.api.PhotosApi
import com.example.photos.api.model.AccessTokenResponse
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class PhotoAuthRepositoryImplTest {
    private lateinit var api: PhotosApi
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var repository: PhotoAuthRepositoryImpl

    @Before
    fun setUp() {
        api = mockk()
        sharedPreferences = mockk()
        editor = mockk()

        // Мокаем SharedPreferences Editor
        every { sharedPreferences.edit() } returns editor
        every { editor.putString(any(), any()) } returns editor
        every { editor.apply() } just Runs

        repository = PhotoAuthRepositoryImpl(api, sharedPreferences)
    }

    @Test
    fun `getAccessToken should save token to SharedPreferences`() = runBlocking {
        // Подготавливаем тестовые данные
        val code = "test_code"
        val token = "test_token"
        val response = AccessTokenResponse(accessToken = token)

        // Мокаем вызов API
        coEvery {
            api.getAccessToken(
                clientId = any(),
                clientSecret = any(),
                redirectUri = any(),
                authorizationCode = code
            )
        } returns response

        // Вызываем метод
        val result = repository.getAccessToken(code)

        // Проверяем, что токен был сохранён
        coVerify {
            api.getAccessToken(
                clientId = any(),
                clientSecret = any(),
                redirectUri = any(),
                authorizationCode = code
            )
        }
        verify {
            sharedPreferences.edit()
            editor.putString("access_token", token)
            editor.apply()
        }

        // Проверяем результат
        assert(result == token)
    }

    @Test
    fun `getToken should return token from SharedPreferences`() {
        // Подготавливаем тестовые данные
        val token = "test_token"
        every { sharedPreferences.getString("access_token", null) } returns token

        // Вызываем метод
        val result = repository.getToken()

        // Проверяем, что SharedPreferences был вызван
        verify { sharedPreferences.getString("access_token", null) }

        // Проверяем результат
        assert(result == token)
    }

    @Test
    fun `saveToken should save token to SharedPreferences`() {
        // Подготавливаем тестовые данные
        val token = "test_token"

        // Вызываем метод
        repository.saveToken(token)

        // Проверяем, что SharedPreferences Editor был вызван
        verify {
            sharedPreferences.edit()
            editor.putString("access_token", token)
            editor.apply()
        }
    }
}
