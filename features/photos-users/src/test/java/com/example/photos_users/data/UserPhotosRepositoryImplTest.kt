package com.example.photos_users.data

import com.example.photos.api.PhotosApi
import com.example.photos.api.model.Photo
import com.example.photos.api.model.Urls
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals


class UserPhotosRepositoryImplTest {

    private lateinit var repository: UserPhotosRepositoryImpl
    private val api: PhotosApi = mockk()

    @Before
    fun setup() {
        repository = UserPhotosRepositoryImpl(api)
    }

    @Test
    fun `getUserPhotos should return photos from API`() = runBlocking {
        // Arrange
        val username = "test_user"
        val page = 1
        val expectedPhotos = listOf(
            Photo(id = "1", urls = Urls("url1"), description = "Photo 1"),
            Photo(id = "2", urls = Urls("url2"), description = "Photo 2")
        )
        coEvery { api.getUserPhotos(username, page) } returns expectedPhotos

        // Act
        val actualPhotos = repository.getUserPhotos(username, page)

        // Assert
        assertEquals(expectedPhotos, actualPhotos)
        coVerify { api.getUserPhotos(username, page) }
    }

    @Test
    fun `getUserPhotos should handle null username gracefully`() = runBlocking {
        // Arrange
        val username: String? = null
        val page = 1
        val expectedPhotos = listOf(
            Photo(id = "1", urls = Urls("url1"), description = "Photo 1"),
        )
        coEvery { api.getUserPhotos("", page) } returns expectedPhotos

        // Act
        val actualPhotos = repository.getUserPhotos(username, page)

        // Assert
        assertEquals(expectedPhotos, actualPhotos)
        coVerify { api.getUserPhotos("", page) }
    }

}
