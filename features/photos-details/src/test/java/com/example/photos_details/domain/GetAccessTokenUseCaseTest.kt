package com.example.photos_details.domain

import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class GetAccessTokenUseCaseTest {
    private val repository: PhotoDetailsRepository = mockk()
    private lateinit var useCase: GetAccessTokenUseCase

    @Before
    fun setUp() {
        useCase = GetAccessTokenUseCase(repository)
    }

    @After
    fun tearDown() {
        clearMocks(repository)
    }

    @Test
    fun `execute should return access token when repository returns success`() = runBlocking {
        // Given
        val clientId = "test-client-id"
        val clientSecret = "test-client-secret"
        val code = "test-code"
        val expectedToken = "access-token"

        coEvery { repository.getAccessToken(clientId, clientSecret, code) } returns expectedToken

        // When
        val result = useCase.execute(clientId, clientSecret, code)

        // Then
        assertEquals(expectedToken, result)

        coVerify(exactly = 1) { repository.getAccessToken(clientId, clientSecret, code) }
        confirmVerified(repository)
    }

    @Test
    fun `execute should throw exception when repository throws error`() = runBlocking {
        // Given
        val clientId = "test-client-id"
        val clientSecret = "test-client-secret"
        val code = "test-code"
        val exception = RuntimeException("Error retrieving access token")

        coEvery { repository.getAccessToken(clientId, clientSecret, code) } throws exception

        // When & Then
        try {
            useCase.execute(clientId, clientSecret, code)
            assert(false) { "Expected exception was not thrown" }
        } catch (e: Exception) {
            assertEquals(exception.message, e.message)
        }

        coVerify(exactly = 1) { repository.getAccessToken(clientId, clientSecret, code) }
        confirmVerified(repository)
    }
}
