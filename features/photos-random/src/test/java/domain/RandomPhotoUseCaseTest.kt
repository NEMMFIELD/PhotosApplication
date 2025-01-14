package domain


import com.example.photos.api.model.Photo
import com.example.photos.api.model.Urls
import data.RandomPhotoMapperFromDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail


class RandomPhotoUseCaseTest {

    private lateinit var randomPhotoRepository: RandomPhotoRepository
    private lateinit var randomPhotoUseCase: RandomPhotoUseCase

    @Before
    fun setUp() {
        randomPhotoRepository = mockk()
        randomPhotoUseCase = RandomPhotoUseCase(randomPhotoRepository)
    }

    @Test
    fun `execute should return mapped list of random photos`() = runBlocking {
        val photos =
            listOf(Photo(id = "1", urls = Urls("url1")), Photo(id = "2", urls = Urls("url2")))
        val expectedPhotos = photos.map { photo ->
            RandomPhotoMapperFromDto.mapDtoToModel(photo)
        }
        // Мокаем репозиторий для возврата Flow
        coEvery { randomPhotoRepository.loadRandomPhotos(2) } returns flowOf(photos).first()

        // Получаем результат из Flow и преобразуем его в список
        val result = randomPhotoUseCase.execute(2).first()

        // Проверяем, что результат соответствует ожидаемому
        assertEquals(expectedPhotos, result)
    }

    @Test
    fun `execute should handle empty response`() = runBlocking {
        coEvery { randomPhotoRepository.loadRandomPhotos(2) } returns emptyList()

        val result = randomPhotoUseCase.execute(2).first()

        assertTrue(result.isEmpty())
        coVerify { randomPhotoRepository.loadRandomPhotos(2) }
    }

    @Test
    fun `execute should handle error`() = runBlocking {
        // Мокируем выбрасывание исключения при загрузке данных
        coEvery { randomPhotoRepository.loadRandomPhotos(2) } throws Exception("Error")
        try {
            // Проверка, что ошибка будет правильно обработана
            randomPhotoUseCase.execute(2).first()
            fail("Exception was expected") // Если исключение не возникло, тест не должен продолжиться
        } catch (e: Exception) {
            // Ожидаем, что исключение было выброшено
            assertTrue(e is Exception)  // Вы можете добавить дополнительную проверку для типа ошибки
        }
        coVerify { randomPhotoRepository.loadRandomPhotos(2) }
    }

    private val mockPhoto = Photo(
        id = "1",
        urls = Urls(
            small = "http://example.com/small.jpg",
            regular = "http://example.com/regular.jpg"
        )
    )
}
