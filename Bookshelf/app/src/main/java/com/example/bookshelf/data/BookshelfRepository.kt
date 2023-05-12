package com.example.bookshelf.data

import com.example.bookshelf.model.Book
import com.example.bookshelf.network.ApiService

interface BookshelfRepository {
    suspend fun getBookshelf(searchTerm: String) : List<Book>
    suspend fun getBook(bookId: Int) : Book
}

class DefaultBookshelfRepository(
    private val apiService: ApiService
) : BookshelfRepository {

    override suspend fun getBookshelf(searchTerm: String): List<Book> {
        return apiService.getBookshelf(searchTerm).items
    }

    override suspend fun getBook(bookId: Int): Book {
        return apiService.getBook(bookId)
    }
}