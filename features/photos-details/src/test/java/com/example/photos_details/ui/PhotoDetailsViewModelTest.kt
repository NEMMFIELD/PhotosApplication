package com.example.photos_details.ui

import android.content.ContentResolver
import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import com.example.photos_details.datali.PhotoDetailsModel
import com.example.photos_details.domain.DislikePhotoUseCase
import com.example.photos_details.domain.DownLoadPhotoUseCase
import com.example.photos_details.domain.GetAccessTokenUseCase
import com.example.photos_details.domain.LikePhotoUseCase
import com.example.photos_details.domain.PhotoDetailsUseCase
import com.example.state.State
import com.example.utils.Logger
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import kotlin.test.Test
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class PhotoDetailsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: PhotoDetailsViewModel

    private val useCase: PhotoDetailsUseCase = mockk()
    private val likePhotoUseCase: LikePhotoUseCase = mockk()
    private val dislikePhotoUseCase: DislikePhotoUseCase = mockk()
    private val sharedPreferences: SharedPreferences = mockk()
    private val logger: Logger = mockk(relaxed = true)
    private val savedStateHandle: SavedStateHandle = mockk()
    private val contentResolver: ContentResolver = mockk()
    private val downLikePhotoUseCase: DownLoadPhotoUseCase = mockk()

    @Before
    fun setUp() {
        every { sharedPreferences.getString("access_token", null) } returns "test_token"
        every { savedStateHandle.get<String>("itemId") } returns "photoId"

        viewModel = PhotoDetailsViewModel(
            useCase,
            likePhotoUseCase,
            dislikePhotoUseCase,
            downLikePhotoUseCase,
            sharedPreferences,
            logger,
            savedStateHandle,
            contentResolver,
        )
    }

    @Test
    fun `postPhoto emits Success state`() = runTest {
        val photoDetailsModel = PhotoDetailsModel(
            id = "photoId",
            description = "Test description",
            pathUrl = "http://example.com/photo.jpg",
            downloads = 100,
            likes = 10,
            likedByUser = false
        )

        // Мокаем useCase
        coEvery { useCase.execute("photoId") } returns flowOf(photoDetailsModel)

        // Запускаем loadSelectedPhoto и подписываемся на пост
        viewModel.loadSelectedPhoto("photoId")

        // Подписываемся на StateFlow, чтобы дождаться изменения состояния
        viewModel.postPhoto
            .filterIsInstance<State.Success<PhotoDetailsModel>>()
            .take(1) // Ожидаем первое обновление состояния
            .collect { state ->
                // Проверяем, что состояние равно Success
                assertTrue(state.data == photoDetailsModel)
            }
    }
}

class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
