package com.example.authorization.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.authorization.domain.PhotoAuthUseCase
import com.example.utils.Logger
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class PhotoAuthViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val logger: Logger = mockk(relaxed = true)
    private lateinit var authUseCase: PhotoAuthUseCase
    private lateinit var viewModel: PhotoAuthViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        authUseCase = mockk()
        viewModel = PhotoAuthViewModel(authUseCase,logger)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `authenticate should update authState with token on success`() = runTest {
        // Подготавливаем тестовые данные
        val code = "test_code"
        val token = "test_token"
        coEvery { authUseCase.execute(code) } returns token

        // Наблюдатель для LiveData
        val observer = mockk<Observer<String>>(relaxed = true)
        viewModel.authState.observeForever(observer)

        // Вызываем метод
        viewModel.authenticate(code)
        testDispatcher.scheduler.advanceUntilIdle() // Ждём выполнения

        // Проверяем, что authState обновился
        coVerify { authUseCase.execute(code) }
        verify { observer.onChanged(token) }

        // Убираем наблюдателя
        viewModel.authState.removeObserver(observer)
    }

    @Test
    fun `authenticate should not crash on failure`() = runTest {
        // Подготавливаем тестовые данные
        val code = "test_code"
        val exception = Exception("Test exception")
        coEvery { authUseCase.execute(code) } throws exception

        // Наблюдатель для LiveData
        val observer = mockk<Observer<String>>(relaxed = true)
        viewModel.authState.observeForever(observer)

        // Вызываем метод
        viewModel.authenticate(code)
        testDispatcher.scheduler.advanceUntilIdle() // Ждём выполнения

        // Проверяем, что метод был вызван
        coVerify { authUseCase.execute(code) }

        // Убираем наблюдателя
        viewModel.authState.removeObserver(observer)
    }


    @Test
    fun `getStoredToken should return token from use case`() {
        // Подготавливаем тестовые данные
        val token = "stored_token"
        every { authUseCase.getStoredToken() } returns token

        // Вызываем метод
        val result = viewModel.getStoredToken()

        // Проверяем вызов метода getStoredToken
        verify { authUseCase.getStoredToken() }

        // Проверяем результат
        assertEquals(token, result)
    }

    @Test
    fun `getStoredToken should return null when no token is stored`() {
        // Мокаем вызов метода getStoredToken
        every { authUseCase.getStoredToken() } returns null

        // Вызываем метод
        val result = viewModel.getStoredToken()

        // Проверяем вызов метода getStoredToken
        verify { authUseCase.getStoredToken() }

        // Проверяем результат
        assertNull(result)
    }
}
