package data

import com.example.photos.api.PhotosApi
import com.example.photos.api.model.Photo
import com.example.photos.api.model.Urls
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith


class RandomPhotoRepositoryImplTest {
    private lateinit var randomPhotosApi: PhotosApi
    private lateinit var randomPhotoRepository: RandomPhotoRepositoryImpl

    @Before
    fun setUp() {
        // Создание моков
        randomPhotosApi = mockk(relaxed = true) // relaxed = true, чтобы не проверять поведение всех методов
        randomPhotoRepository = RandomPhotoRepositoryImpl(randomPhotosApi)
    }

    @Test
    fun `loadRandomPhotos should return list of photos when API call is successful`() = runBlocking {
        // Подготовка данных для мока
        val mockPhotos = listOf(Photo(id = "1",  urls = Urls(small ="http://example.com/photo1)")),
                                Photo(id = "2", urls =  Urls(small ="http://example.com/photo2)")))

        // Мокаем успешный ответ от API
        coEvery { randomPhotosApi.getRandomPhotos(2) } returns mockPhotos

        // Вызов тестируемого метода
        val result = randomPhotoRepository.loadRandomPhotos(2)

        // Проверки
        assertEquals(mockPhotos, result) // Проверка, что результат соответствует ожидаемым данным
        coVerify { randomPhotosApi.getRandomPhotos(2) } // Проверка, что API был вызван с правильными параметрами
    }

    @Test
    fun `loadRandomPhotos should throw exception when API call fails`() = runBlocking {
        // Мокаем ошибку от API
        coEvery { randomPhotosApi.getRandomPhotos(2) } throws Exception("API call failed")

        // Проверка, что при вызове метода будет выброшено исключение
        assertFailsWith<Exception> {
            randomPhotoRepository.loadRandomPhotos(2)
        }

        // Проверка, что API был вызван
        coVerify { randomPhotosApi.getRandomPhotos(2) }
    }
}
