package com.example.bookshelf

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.bookshelf.data.BookshelfRepository
import com.example.bookshelf.model.Book
import com.example.bookshelf.model.VolumeInfo
import com.example.bookshelf.ui.screens.BookshelfUIState
import com.example.bookshelf.ui.screens.BookshelfViewModel
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.io.IOException

class BookshelfViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: BookshelfViewModel

    @Mock
    private lateinit var bookshelfRepository: BookshelfRepository
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = BookshelfViewModel(bookshelfRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getBookshelf returns data`() = runTest {
        // Given a list of books
        val books = listOf(
            Book("1", VolumeInfo("Book 1", authors = listOf("Author A"))),
            Book("2", VolumeInfo("Book 2", authors = listOf("Author B")))
        )
        `when`(bookshelfRepository.getBookshelf(" ")).thenReturn(books)

        // When calling getBookshelf
        viewModel.getBookshelf(" ")

        // Then the bookshelfUIState should be Success with the list of books
        assertTrue(viewModel.bookshelfUIState is BookshelfUIState.Success)
        val successState = viewModel.bookshelfUIState as BookshelfUIState.Success
        assertTrue(successState.bookshelfList == books)
    }

    @Test
    fun `getBookshelf returns error`() = runTest {
        // Given an RunTimeException is thrown by the repository
        `when`(bookshelfRepository.getBookshelf(" ")).thenThrow(RuntimeException::class.java)

        // When calling getBookshelf
        viewModel.getBookshelf(" ")

        // Then the bookshelfUIState should be Error
        assertTrue(viewModel.bookshelfUIState is BookshelfUIState.Error)
    }

    @Test
    fun `getBookshelf returns empty list`() = runTest {
        // Given an empty list of books
        `when`(bookshelfRepository.getBookshelf(" ")).thenReturn(emptyList())

        // When calling getBookshelf
        viewModel.getBookshelf(" ")

        // Then the bookshelfUIState should be Success with an empty list
        assertTrue(viewModel.bookshelfUIState is BookshelfUIState.Success)
        val successState = viewModel.bookshelfUIState as BookshelfUIState.Success
        assertTrue(successState.bookshelfList.isEmpty())
    }
}