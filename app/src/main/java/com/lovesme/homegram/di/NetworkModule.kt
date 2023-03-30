package com.lovesme.homegram.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.lovesme.homegram.util.network.CloudMessagingService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton
import com.lovesme.homegram.BuildConfig

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val CLOUD_MESSAGE_BASE_URL = "https://fcm.googleapis.com"
    private val contentType = "application/json".toMediaType()

    @Singleton
    @Provides
    fun provideCloudMessageInterceptor() = Interceptor { chain ->
        with(chain) {
            val request = request()
                .newBuilder()
                .addHeader("Authorization", "key=${BuildConfig.CLOUD_MESSAGING_SERVER_KEY}")
                .build()
            proceed(request)
        }
    }

    @Singleton
    @Provides
    fun provideCloudMessageOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideCloudMessageRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .baseUrl(CLOUD_MESSAGE_BASE_URL)
        .build()

    @Singleton
    @Provides
    fun provideCloudMessageService(retrofit: Retrofit): CloudMessagingService =
        retrofit.create(CloudMessagingService::class.java)
}