package com.example.photos.search.domain

import app.cash.turbine.test
import com.example.photos.api.model.Photo
import com.example.photos.api.model.SearchPhotosResponse
import com.example.photos.api.model.Urls
import com.example.photos.search.data.PhotoModel
import com.example.photos.search.data.PhotoModelMapperFromDto
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals


class SearchPhotosUseCaseTest {
    private val repository: SearchPhotosRepository = mockk()
    private lateinit var useCase: SearchPhotosUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        useCase = SearchPhotosUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `execute emits mapped list when query is not empty`() = runTest {
        // Arrange
        val query = "nature"
        val page = 1
        val photoDtoList = listOf(
            Photo(id = "1", urls = Urls("url1"), description = "desc1"),
            Photo(id = "2", urls = Urls("url2"), description = "desc2")
        )
        val expectedModelList = photoDtoList.map {
            PhotoModelMapperFromDto.mapDtoToModel(it)
        }
        val response = SearchPhotosResponse(total = 2, total_pages = 1, results = photoDtoList)

        coEvery { repository.searchPhotos(query, page) } returns response

        // Act
        useCase.execute(query, page).test {
            val result = awaitItem()

            // Assert
            assertEquals(expectedModelList, result)
            awaitComplete()
        }

        // Verify that the repository was called
        coVerify(exactly = 1) { repository.searchPhotos(query, page) }
    }

    @Test
    fun `execute emits empty list when query is empty`() = runTest {
        // Arrange
        val query = ""
        val page = 1

        // Act
        useCase.execute(query, page).test {
            val result = awaitItem()

            // Assert
            assertEquals(emptyList<PhotoModel>(), result)
            awaitComplete()
        }

        // Verify that the repository was not called
        coVerify { repository wasNot Called }
    }
}
