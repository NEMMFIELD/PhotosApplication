package com.example.photos.search.data

import com.example.photos.api.PhotosApi
import com.example.photos.api.model.SearchPhotosResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


class SearchPhotosRepositoryImplTest {
    private lateinit var repository: SearchPhotosRepositoryImpl
    private val api: PhotosApi = mockk()

    @Before
    fun setUp() {
        repository = SearchPhotosRepositoryImpl(api)
    }

    @Test
    fun `searchPhotos calls API with correct parameters and returns response`() = runBlocking {
        // Arrange
        val query = "nature"
        val page = 1
        val expectedResponse = SearchPhotosResponse(
            total = 100,
           total_pages = 10,
            results = emptyList() // Примерный формат ответа
        )
        coEvery { api.searchPhotos(query, page) } returns expectedResponse

        // Act
        val result = repository.searchPhotos(query, page)

        // Assert
        coVerify(exactly = 1) { api.searchPhotos(query, page) }
        assertEquals(expectedResponse, result)
    }

    @Test
    fun `searchPhotos propagates exception from API`(): Unit = runBlocking {
        // Arrange
        val query = "city"
        val page = 2
        val exception = Exception("Network error")
        coEvery { api.searchPhotos(query, page) } throws exception

        // Act & Assert
        val thrownException = kotlin.runCatching {
            repository.searchPhotos(query, page)
        }.exceptionOrNull()

        // Проверяем, что было выброшено ожидаемое исключение
        assertEquals(exception, thrownException)
    }
}
