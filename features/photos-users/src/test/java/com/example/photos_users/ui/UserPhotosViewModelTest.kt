package com.example.photos_users.ui

import androidx.lifecycle.SavedStateHandle
import com.example.photos_users.data.UserPhotosModel
import com.example.photos_users.domain.GetUserPhotosUseCase
import com.example.state.State
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class UserPhotosViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule() // Правило для замены Dispatchers.Main на TestDispatcher

    private val userPhotosUseCase: GetUserPhotosUseCase = mockk()
    private val savedStateHandle: SavedStateHandle = mockk()
    private lateinit var viewModel: UserPhotosViewModel

    @Before
    fun setUp() {
        every { savedStateHandle.get<String>("username") } returns "test_user"
        every { savedStateHandle.get<String>("name") } returns "Test Name"
        viewModel = UserPhotosViewModel(userPhotosUseCase, savedStateHandle)
    }

    @Test
    fun `loadUserPhotos should load photos successfully`() = runTest {
        // Arrange
        val photos = listOf(
            UserPhotosModel("1", "Desc1", "url1", "user1", "User One"),
            UserPhotosModel("2", "Desc2", "url2", "user2", "User Two")
        )
        coEvery { userPhotosUseCase.execute("test_user", 1) } returns flowOf(photos)

        // Act
        viewModel.loadUserPhotos()

        // Assert
        val result = viewModel.postUserPhotos.first()
        assertTrue(result is State.Success)
        assertEquals(photos, (result as State.Success).data)
    }

    @Test
    fun `loadUserPhotos should handle empty photo list`() = runTest {
        // Arrange
        val savedStateHandle = SavedStateHandle(mapOf("username" to "test_user"))
        val userPhotosUseCase: GetUserPhotosUseCase = mockk()
        val viewModel = UserPhotosViewModel(userPhotosUseCase, savedStateHandle)

        coEvery { userPhotosUseCase.execute("test_user", 1) } returns flowOf(emptyList())

        // Act
        viewModel.loadUserPhotos()
        advanceUntilIdle() // Ждем завершения всех корутин

        // Assert
        val result = viewModel.postUserPhotos.first()
        assertTrue(result is State.Empty) // Проверяем, что состояние пустое
    }

    @Test
    fun `loadUserPhotos should handle errors`() = runTest {
        // Arrange
        val exception = Exception("Network Error")
        coEvery { userPhotosUseCase.execute("test_user", 1) } throws exception

        // Act
        viewModel.loadUserPhotos()

        // Assert
        val result = viewModel.postUserPhotos.first()
        assertTrue(result is State.Failure)
        assertEquals(exception, (result as State.Failure).message)
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `loadNextPage should load next page successfully`() = runTest {
        // Arrange
        val savedStateHandle = SavedStateHandle(mapOf("username" to "test_user"))
        val viewModel = UserPhotosViewModel(userPhotosUseCase, savedStateHandle)
        val exceptionList = listOf(
            UserPhotosModel("id1", "desc1", "url1", "username1", "name1"),
            UserPhotosModel("id2", "desc2", "url2", "username2", "name2"),
            UserPhotosModel("id3", "desc3", "url3", "username3", "name3"),
            UserPhotosModel("id4", "desc4", "url4", "username4", "name4")
        )

        val page1Photos = listOf(
            UserPhotosModel("id1", "desc1", "url1", "username1", "name1"),
            UserPhotosModel("id2", "desc2", "url2", "username2", "name2")
        )
        val page2Photos = listOf(
            UserPhotosModel("id3", "desc3", "url3", "username3", "name3"),
            UserPhotosModel("id4", "desc4", "url4", "username4", "name4")
        )

        // Mocking responses
        coEvery { userPhotosUseCase.execute("test_user", 1) } returns flowOf(page1Photos)
        coEvery { userPhotosUseCase.execute("test_user", 2) } returns flowOf(page2Photos)

        // Act: Load the first page
        viewModel.loadUserPhotos()
        advanceUntilIdle() // Wait for coroutines to complete

        // Assert: Verify first page is loaded
        val resultPage1 = viewModel.postUserPhotos.first()
        assertTrue(resultPage1 is State.Success)
        assertEquals(page1Photos, (resultPage1 as State.Success).data)

        // Act: Load the second page
        viewModel.loadNextPage()
        advanceUntilIdle() // Wait for coroutines to complete

        // Assert: Verify combined result
        val resultPage2 = viewModel.postUserPhotos.first()
        assertTrue(resultPage2 is State.Success)

        val combinedPhotos = page1Photos + page2Photos
        assertEquals(combinedPhotos, exceptionList)
    }

    @Test
    fun `onCleared should reset state`() = runTest {
        // Arrange
        val photos = listOf(
            UserPhotosModel("1", "Desc1", "url1", "user1", "User One")
        )
        coEvery { userPhotosUseCase.execute("test_user", 1) } returns flowOf(photos)

        viewModel.loadUserPhotos()

        // Act
        viewModel.onCleared()

        // Assert
        val result = viewModel.postUserPhotos.first()
        assertTrue(result is State.Empty)
    }
}

@ExperimentalCoroutinesApi
class MainDispatcherRule(
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
