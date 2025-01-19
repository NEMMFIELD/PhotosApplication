package com.example.photosapplication.di

import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import com.example.photos.api.PhotosApi
import com.example.photos_random.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://api.unsplash.com/"

    @Provides
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun provideAuthInterceptor(
        sharedPreferences: SharedPreferences
    ): Interceptor {
        val accessKey = BuildConfig.ACCESS_KEY
        return Interceptor { chain ->
            val request = chain.request()
            val token = sharedPreferences.getString("access_token", null)

            val newRequest = request.newBuilder().apply {
                if (token != null) {
                    addHeader("Authorization", "Bearer $token")
                } else {
                    url(request.url.newBuilder().addQueryParameter("client_id", accessKey).build())
                }
            }.build()

            chain.proceed(newRequest)
        }
    }

    @Singleton
    @Provides
    fun provideTokenAuthenticator(
        apiProvider: Provider<PhotosApi>, // Используем Provider для разрыва цикла
        sharedPreferences: SharedPreferences
    ): Authenticator {
        return Authenticator { _, response ->
            val clientId = BuildConfig.ACCESS_KEY
            val clientSecret = BuildConfig.SECRET_KEY
            val refreshToken =
                sharedPreferences.getString("refresh_token", null) ?: return@Authenticator null

            try {
                val newToken = runBlocking {
                    apiProvider.get().getAccessToken(
                        clientId = clientId,
                        clientSecret = clientSecret,
                        redirectUri = "urn:ietf:wg:oauth:2.0:oob",
                        authorizationCode = refreshToken
                    ).accessToken
                }
                sharedPreferences.edit().putString("access_token", newToken).apply()

                response.request.newBuilder()
                    .header("Authorization", "Bearer $newToken")
                    .build()
            } catch (e: Exception) {
                null // Возвращаем null, чтобы аутентификация провалилась
            }
        }
    }

    @Singleton
    @Provides
    fun provideCache(@ApplicationContext context: Context): Cache {
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
        return Cache(context.cacheDir, cacheSize)
    }

    @Singleton
    @Provides
    fun provideHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: Interceptor,
        tokenAuthenticator: Authenticator,
        cache: Cache
    ): OkHttpClient =
        OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(authInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .authenticator(tokenAuthenticator)
            .connectTimeout(30, TimeUnit.SECONDS) // Таймаут подключения
            .readTimeout(30, TimeUnit.SECONDS)    // Таймаут чтения
            .writeTimeout(30, TimeUnit.SECONDS)   // Таймаут записи
            .build()

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
        .build()

    @Provides
    @Singleton
    fun providePhotosApi(retrofit: Retrofit):PhotosApi = retrofit.create(PhotosApi::class.java)
}


