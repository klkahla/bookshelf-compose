package com.example.bookshelf.data

import com.example.bookshelf.network.ApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val bookshelfRepository: BookshelfRepository
}


@ExperimentalSerializationApi
class DefaultAppContainer : AppContainer {
    private val BASE_URL = "https://www.googleapis.com/books/v1/"

    /**
     * Configure JSON for kotlinx.serialization
     */
    private val kotlinxSerializationJSON = Json { ignoreUnknownKeys = true; explicitNulls = false }

    /**
     * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
     */
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(kotlinxSerializationJSON.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    /**
     * Retrofit service object for creating api calls
     */
    private val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    override val bookshelfRepository: BookshelfRepository by lazy {
        DefaultBookshelfRepository(retrofitService)
    }
}