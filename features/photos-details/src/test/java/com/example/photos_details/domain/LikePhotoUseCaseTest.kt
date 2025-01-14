package com.example.photos_details.domain

import com.example.photos.api.model.LikePhotoResponse
import com.example.photos.api.model.Photo
import com.example.photos.api.model.Urls
import com.example.photos.api.model.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class LikePhotoUseCaseTest {
    private val repository: PhotoDetailsRepository = mockk()
    private lateinit var useCase: LikePhotoUseCase
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        useCase = LikePhotoUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `execute should emit LikePhotoResponse when repository returns success`() = runTest {
        // Given
        val photoId = "123"
        val photo = Photo(id = "123", urls=  Urls("http://example.com/photo.jpg)"))
        val user = User(id = "456", name = "John Doe")
        val likeResponse = LikePhotoResponse(photo = photo, user = user)

        coEvery { repository.likePhoto(photoId) } returns likeResponse

        // When
        val result = useCase.execute(photoId).toList()

        // Then
        assertEquals(listOf(likeResponse), result)

        coVerify(exactly = 1) { repository.likePhoto(photoId) }
        confirmVerified(repository)
    }

    @Test
    fun `execute should emit empty LikePhotoResponse on failure`() = runTest {
        // Given
        val photoId = "123"
        val exception = RuntimeException("Error liking photo")

        coEvery { repository.likePhoto(photoId) } throws exception

        // When
        val result = useCase.execute(photoId)
            .catch { emit(LikePhotoResponse(photo = Photo(null, ""), user = User(null, null))) }
            .toList()

        // Then
        assertEquals(
            listOf(LikePhotoResponse(photo = Photo(null, ""), user = User(null, null))),
            result
        )

        coVerify(exactly = 1) { repository.likePhoto(photoId) }
        confirmVerified(repository)
    }
}
