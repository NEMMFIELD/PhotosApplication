package com.example.photos_details.domain

import com.example.photos.api.model.Photo
import com.example.photos.api.model.Urls
import com.example.photos_details.data.PhotoDetailsMapperFromDto
import com.example.photos_details.datali.PhotoDetailsModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class PhotoDetailsUseCaseTest {
    // Mocked dependencies
    private val repository: PhotoDetailsRepository = mockk()
    private lateinit var useCase: PhotoDetailsUseCase

    // Test dispatcher
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        useCase = PhotoDetailsUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `execute should emit mapped PhotoDetailsModel when repository returns data`() = runTest {
        // Given
        val id = "123"
        val photoDto =
            Photo(id = id, description = "Test Photo", urls = Urls("http://example.com/photo.jpg)"))
        val photoDetailsModel = PhotoDetailsModel(
            id = id,
            description = "Test Photo",
            pathUrl = "http://example.com/photo.jpg",
            downloads = 100,
            likes = 50,
            likedByUser = false
        )

        coEvery { repository.loadPhoto(id) } returns photoDto
        mockkObject(PhotoDetailsMapperFromDto)
        every { PhotoDetailsMapperFromDto.mapDtoToDetailsModel(photoDto) } returns photoDetailsModel

        // When
        val result = useCase.execute(id).toList()

        // Then
        assertEquals(listOf(photoDetailsModel), result)

        coVerify(exactly = 1) { repository.loadPhoto(id) }
        verify(exactly = 1) { PhotoDetailsMapperFromDto.mapDtoToDetailsModel(photoDto) }

        confirmVerified(repository, PhotoDetailsMapperFromDto)
    }
}
