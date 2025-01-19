package com.example.photos.search.ui

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.photos.api.model.Photo
import com.example.photos.search.data.PhotoModel
import com.example.photos.search.domain.SearchPhotosUseCase
import com.example.state.State
import com.example.utils.Logger
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals


class SearchPhotosViewModelTest {
    private val searchPhotosUseCase: SearchPhotosUseCase = mockk()
    private val savedStateHandle: SavedStateHandle = mockk()
    private val logger: Logger = mockk(relaxed = true)
    private lateinit var viewModel: SearchPhotosViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SearchPhotosViewModel(searchPhotosUseCase, savedStateHandle, logger)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `searchPhotos updates allPhotos with success state`() = runTest {
        // Arrange
        val query = "nature"
        val photoList = listOf(PhotoModel(id = "1", pathUrl = "url1", description = "desc1"))
        coEvery { searchPhotosUseCase.execute(query, 1) } returns flowOf(photoList)

        // Act
        viewModel.searchPhotos(query)

        // Assert
        viewModel.allPhotos.test {
            assertEquals(State.Empty, awaitItem()) // Initial state
            assertEquals(State.Success(photoList), awaitItem()) // Success state
            cancelAndIgnoreRemainingEvents()
        }
        coVerify { searchPhotosUseCase.execute(query, 1) }
    }

    @Test
    fun `searchPhotos updates allPhotos with failure state when exception occurs`() = runTest {
        // Arrange

        val query = "error"
        val exception = Exception("Network error")
        coEvery { searchPhotosUseCase.execute(query, 1) } throws exception

        // Act
        viewModel.searchPhotos(query)

        // Assert
        viewModel.allPhotos.test {
            assertEquals(State.Empty, awaitItem()) // Initial state
            val failureState = awaitItem() as State.Failure
            assertEquals(exception.message, failureState.message.message) // Compare messages
            cancelAndIgnoreRemainingEvents()
        }
        coVerify { searchPhotosUseCase.execute(query, 1) }
    }

    @Test
    fun `loadMorePhotos appends new photos to allPhotos`() = runTest {
        // Arrange
        val query = "nature"
        val initialPhotos = listOf(PhotoModel(id = "1", pathUrl = "url1", description = "desc1"))
        val newPhotos = listOf(PhotoModel(id = "2", pathUrl = "url2", description = "desc2"))

        coEvery { searchPhotosUseCase.execute(query, 1) } returns flowOf(initialPhotos)
        coEvery { searchPhotosUseCase.execute(query, 2) } returns flowOf(newPhotos)

        // Act: запускаем поиск фотографий
        viewModel.searchPhotos(query)

        // Assert: проверяем, что начальное состояние Empty
        viewModel.allPhotos.test {
            // Ждем состояния Empty
            assertEquals(State.Empty, awaitItem())  // Ожидаем первое состояние как Empty

            // Загружаем фотографии
            advanceUntilIdle()  // Ожидаем завершения первого запроса

            // После завершения запроса должны увидеть успех с initialPhotos
            assertEquals(State.Success(initialPhotos), awaitItem())

            // Загружаем дополнительные фотографии
            viewModel.loadMorePhotos()
            advanceUntilIdle()  // Ожидаем завершения второго запроса

            // Проверяем, что новые фотографии были добавлены
            assertEquals(State.Success(initialPhotos + newPhotos), awaitItem())
        }

        // Проверяем, что use case вызван дважды: для первой и второй страницы
        coVerify(exactly = 1) { searchPhotosUseCase.execute(query, 1) }
        coVerify(exactly = 1) { searchPhotosUseCase.execute(query, 2) }
    }
}
