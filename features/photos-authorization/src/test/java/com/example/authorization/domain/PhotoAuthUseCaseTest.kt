package com.example.authorization.domain

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class PhotoAuthUseCaseTest {
    private lateinit var repository: PhotoAuthRepository
    private lateinit var useCase: PhotoAuthUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = PhotoAuthUseCase(repository)
    }

    @Test
    fun `execute should return access token from repository`() = runBlocking {
        // Подготавливаем тестовые данные
        val code = "test_code"
        val token = "test_token"

        // Мокаем вызов метода getAccessToken
        coEvery { repository.getAccessToken(code) } returns token

        // Вызываем метод
        val result = useCase.execute(code)

        // Проверяем вызов метода getAccessToken
        coVerify { repository.getAccessToken(code) }

        // Проверяем результат
        assertEquals(token, result)
    }

    @Test
    fun `getStoredToken should return token from repository`() {
        // Подготавливаем тестовые данные
        val token = "stored_token"

        // Мокаем вызов метода getToken
        every { repository.getToken() } returns token

        // Вызываем метод
        val result = useCase.getStoredToken()

        // Проверяем вызов метода getToken
        verify { repository.getToken() }

        // Проверяем результат
        assertEquals(token, result)
    }

    @Test
    fun `getStoredToken should return null if no token stored`() {
        // Мокаем вызов метода getToken
        every { repository.getToken() } returns null

        // Вызываем метод
        val result = useCase.getStoredToken()

        // Проверяем вызов метода getToken
        verify { repository.getToken() }

        // Проверяем результат
        assertNull(result)
    }
}
