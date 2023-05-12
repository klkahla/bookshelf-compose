package com.example.bookshelf.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookshelf.BookshelfApplication
import com.example.bookshelf.data.BookshelfRepository
import com.example.bookshelf.model.Book
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface  BookshelfUIState {
    data class Success(val bookshelfList: List<Book>) : BookshelfUIState
    object Error : BookshelfUIState
    object Loading : BookshelfUIState
}

class BookshelfViewModel(private val bookshelfRepository: BookshelfRepository): ViewModel() {
    var bookshelfUIState: BookshelfUIState by mutableStateOf(BookshelfUIState.Loading)
        private set

    init {
        getBookshelf("")
    }

    fun getBookshelf(searchTerm: String) {
        var searchTerm = searchTerm
        if (searchTerm.isEmpty()) {
            searchTerm = "%20"
        }
        viewModelScope.launch {
            bookshelfUIState = BookshelfUIState.Loading
            bookshelfUIState = try {
                BookshelfUIState.Success(bookshelfRepository.getBookshelf(searchTerm))
            } catch (e: IOException) {
                BookshelfUIState.Error
            } catch (e: HttpException) {
                BookshelfUIState.Error
            }
        }
    }

    /**
     * Factory for [BookshelfViewModel] that takes [BookshelfRepository] as a dependency
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BookshelfApplication)
                val bookshelfRepository = application.container.bookshelfRepository
                BookshelfViewModel(bookshelfRepository = bookshelfRepository)
            }
        }
    }
}