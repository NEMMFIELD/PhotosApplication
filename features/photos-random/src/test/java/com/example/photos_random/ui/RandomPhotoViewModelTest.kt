package com.example.photos_random.ui

import app.cash.turbine.test
import com.example.state.State
import data.RandomPhotoModel
import domain.RandomPhotoUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


@ExperimentalCoroutinesApi
class RandomPhotoViewModelTest {
    private lateinit var randomPhotoViewModel: RandomPhotoViewModel
    private val useCase: RandomPhotoUseCase = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher) // Устанавливаем тестовый диспетчер
        randomPhotoViewModel = RandomPhotoViewModel(useCase)
    }

    @Test
    fun `test postRandomPhoto state success`() = runTest {
        // Arrange
        val randomPhotos = listOf(
            RandomPhotoModel(id = "1", pathUrl = "url1", description = "test1"),
            RandomPhotoModel(id = "2", pathUrl = "url2", description = "test2")
        )

        // Мокаем execute, чтобы он возвращал flow с randomPhotos
        coEvery { useCase.execute(RANDOM_PHOTO_COUNT) } returns flowOf(randomPhotos)

        // Act - подписываемся на поток и проверяем результат
        randomPhotoViewModel.postRandomPhoto.test {
            // Ждем, пока поток не выдаст результат
            awaitItem() // Ждем первого значения, которое будет State.Empty
            val state = awaitItem() as? State.Success<List<RandomPhotoModel>>
            assertTrue(state?.data == randomPhotos) // Проверяем, что результат соответствует ожидаемому
            cancelAndIgnoreRemainingEvents() // Завершаем тест
        }

        // Выполняем все отложенные задачи
        testDispatcher.scheduler.advanceUntilIdle()
    }

    @Test
    fun `test postRandomPhoto state failure`() = runTest {
        // Arrange
        val throwable = Throwable("Network error")

        // Мокаем execute, чтобы он выбрасывал ошибку
        coEvery { useCase.execute(RANDOM_PHOTO_COUNT) } throws throwable

        // Act - подписываемся на поток и проверяем результат
        randomPhotoViewModel.postRandomPhoto.test {
            awaitItem() // Ждем первого значения, которое будет State.Empty
            val state = awaitItem() as? State.Failure
            assertEquals(
                state?.message?.message,
                throwable.message
            ) // Проверяем, что ошибка корректно обработана
        }

        // Act - Запускаем корутины для выполнения
        testDispatcher.scheduler.advanceUntilIdle() // Выполняем все отложенные задачи
    }
}
