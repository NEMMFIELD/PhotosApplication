package com.example.photos_users.domain

import com.example.photos.api.model.Photo
import com.example.photos.api.model.Urls
import com.example.photos.api.model.User
import com.example.photos_users.data.UserPhotosMapperFromDto
import com.example.photos_users.data.UserPhotosModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.mockito.Mockito.verify
import kotlin.test.Test
import kotlin.test.assertEquals


class GetUserPhotosUseCaseTest {

    private lateinit var useCase: GetUserPhotosUseCase
    private val repository: UserPhotosRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = GetUserPhotosUseCase(repository)
    }

    @Test
    fun `execute should return mapped user photos`() = runTest {
        // Arrange
        val username = "test_user"
        val page = 1
        val photoDtoList = listOf(
            Photo(id = "1", urls = Urls("url1"), description = "Photo 1"),
            Photo(id = "2", urls = Urls("url2"), description = "Photo 2")
        )
        val expectedModels = photoDtoList.map { dto ->
            UserPhotosMapperFromDto.mapDtoToModel(dto)
        }

        coEvery { repository.getUserPhotos(username, page) } returns photoDtoList

        // Act
        val result = useCase.execute(username, page).first()

        // Assert
        assertEquals(expectedModels, result) // Сравнение по содержимому
    }

    @Test
    fun `execute should handle null username gracefully`() = runTest {
        // Arrange
        val page = 1
        val photoDtoList = listOf(
            Photo(
                id = "1",
                altDescription = "Description 1",
                urls = Urls(small = "url1"),
                user = User(username = "user1", name = "User One")
            )
        )
        val expectedModels = listOf(
            UserPhotosModel(
                photoId = "1",
                photoDescription = "Description 1",
                pathUrl = "url1",
                userName = "user1",
                name = "User One"
            )
        )

        // Обрабатываем null в username
        coEvery { repository.getUserPhotos(isNull(), page) } returns photoDtoList

        // Act
        val result = useCase.execute(null, page).first()

        // Assert
        assertEquals(expectedModels, result)
    }
}
