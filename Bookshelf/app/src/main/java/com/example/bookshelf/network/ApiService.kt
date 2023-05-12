package com.example.bookshelf.network

import com.example.bookshelf.model.Book
import com.example.bookshelf.model.Bookshelf
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("volumes")
    suspend fun getBookshelf(@Query("q") searchTerm: String): Bookshelf

    @GET("volumes/{id}")
    suspend fun getBook(id: Int): Book
}