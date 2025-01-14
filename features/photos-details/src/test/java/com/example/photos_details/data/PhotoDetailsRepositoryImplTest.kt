package com.example.photos_details.data

import com.example.photos.api.PhotosApi
import com.example.photos.api.model.AccessTokenResponse
import com.example.photos.api.model.LikePhotoResponse
import com.example.photos.api.model.Photo
import com.example.photos.api.model.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PhotoDetailsRepositoryImplTest {
    private lateinit var api: PhotosApi
    private lateinit var repository: PhotoDetailsRepositoryImpl

    @Before
    fun setUp() {
        api = mockk()
        repository = PhotoDetailsRepositoryImpl(api)
    }

    @Test
    fun `loadPhoto should return photo from API`() = runBlocking {
        // Given
        val photoId = "123"
        val expectedPhoto = Photo(id = photoId, description = "Sample Photo")
        coEvery { api.getPhoto(photoId) } returns expectedPhoto

        // When
        val result = repository.loadPhoto(photoId)

        // Then
        coVerify { api.getPhoto(photoId) }
        assertEquals(expectedPhoto, result)
    }

    @Test
    fun `getAccessToken should return access token from API`() = runBlocking {
        // Given
        val clientId = "client_id"
        val clientSecret = "client_secret"
        val code = "auth_code"
        val expectedToken = "access_token"
        val response = AccessTokenResponse(accessToken = expectedToken)

        coEvery {
            api.getAccessToken(
                clientId = clientId,
                clientSecret = clientSecret,
                redirectUri = "urn:ietf:wg:oauth:2.0:oob",
                grantType = "authorization_code",
                authorizationCode = code
            )
        } returns response

        // When
        val result = repository.getAccessToken(clientId, clientSecret, code)

        // Then
        coVerify {
            api.getAccessToken(
                clientId = clientId,
                clientSecret = clientSecret,
                redirectUri = "urn:ietf:wg:oauth:2.0:oob",
                grantType = "authorization_code",
                authorizationCode = code
            )
        }
        assertEquals(expectedToken, result)
    }

    @Test
    fun `likePhoto should return like response from API`() = runBlocking {
        // Given
        val photoId = "123"
        val expectedPhoto = Photo(id = photoId, description = "Sample Photo")
        val expectedUser = User(id = "user_1", name = "John Doe")
        val expectedResponse = LikePhotoResponse(photo = expectedPhoto, user = expectedUser)

        coEvery { api.likePhoto(photoId) } returns expectedResponse

        // When
        val result = repository.likePhoto(photoId)

        // Then
        coVerify { api.likePhoto(photoId) }
        assertEquals(expectedResponse.photo, result.photo)
        assertEquals(expectedResponse.user, result.user)
    }

    @Test
    fun `dislikePhoto should return dislike response from API`() = runBlocking {
        // Given
        val photoId = "123"
        val expectedPhoto = Photo(id = photoId, description = "Sample Photo")
        val expectedUser = User(id = "user_1", name = "John Doe")
        val expectedResponse = LikePhotoResponse(photo = expectedPhoto, user = expectedUser)

        coEvery { api.dislikePhoto(photoId) } returns expectedResponse

        // When
        val result = repository.dislikePhoto(photoId)

        // Then
        coVerify { api.dislikePhoto(photoId) }
        assertEquals(expectedResponse.photo, result.photo)
        assertEquals(expectedResponse.user, result.user)
    }
}
