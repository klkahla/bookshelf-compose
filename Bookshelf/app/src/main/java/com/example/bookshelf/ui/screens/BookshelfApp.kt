@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.bookshelf.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookshelf.R
import com.example.bookshelf.model.Book
import com.example.bookshelf.model.ImageLinks
import com.example.bookshelf.model.VolumeInfo
import com.example.bookshelf.ui.theme.BookshelfTheme

@Composable
fun BookshelfApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colorScheme.background
        ) {
            val bookshelfViewModel: BookshelfViewModel = viewModel(factory = BookshelfViewModel.Factory)
            var searchTerm by remember {mutableStateOf("")}

            NavHost(navController = navController, startDestination = "bookList") {
                composable("bookList") {
                    BookList(
                        navController = navController,
                        bookshelfViewModel.bookshelfUIState,
                        searchTerm = searchTerm,
                        onSearchTermChanged = {
                            searchTerm = it
                            bookshelfViewModel.getBookshelf(it)
                        },
                        retryAction = bookshelfViewModel::getBookshelf
                    )
                }
                composable("bookDetail/{bookId}") { backStackEntry ->
                    val bookId = backStackEntry.arguments?.getString("bookId", "-1").toString()
                    val book = bookshelfViewModel.getBookById(bookId)
                    if (book != null) {
                        BookDetail(book = book)
                    }
                }
            }

        }
    }
}

@Composable
fun BookList(navController: NavHostController, bookshelfUIState: BookshelfUIState, searchTerm: String, onSearchTermChanged: (String) -> Unit, retryAction: (String) -> Unit, modifier: Modifier = Modifier) {
    Column {
        SearchView(searchTerm, onSearchTermChanged)
        when (bookshelfUIState) {
            is BookshelfUIState.Loading -> LoadingScreen(modifier)
            is BookshelfUIState.Error -> ErrorScreen(retryAction = retryAction, modifier)
            is BookshelfUIState.Success -> ResultScreen(
                navController = navController,
                bookshelfUIState.bookshelfList,
                modifier
            )
        }
    }
}

@Composable
fun BookCard(book: Book, navController: NavHostController, modifier: Modifier = Modifier) {
    val thumbnailsUrl = book.volumeInfo?.imageLinks?.thumbnail?.replace("http://", "https://")

    Card (
        modifier = Modifier
            .padding(8.dp)
            .clickable { navController.navigate("bookDetail/${book.id}") }
        ,
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                modifier = Modifier.fillMaxWidth(),
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(thumbnailsUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.loading_img)
            )
        }
    }
}

@Composable
fun ErrorScreen(retryAction: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.loading_failed))
        Button(onClick = { retryAction("") }) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier,
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun ResultScreen(navController: NavHostController, bookList: List<Book>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        modifier = modifier
    ) {
        items(bookList) { book ->
            BookCard(
                book = book,
                navController = navController
            )
        }
    }
}

@Composable
fun SearchView(searchTerm: String, onSearchTermChanged: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = searchTerm,
        onValueChange = onSearchTermChanged,
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (searchTerm.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onSearchTermChanged("")
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RectangleShape, // The TextFiled has rounded corners top left and right by defaul
    )
}

//@Preview(showBackground = true)
//@Composable
//fun LoadingScreenPreview() {
//    BookshelfTheme {
//        LoadingScreen(
//            Modifier
//                .fillMaxSize()
//                .size(200.dp))
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun ErrorScreenPreview() {
//    BookshelfTheme {
//        ErrorScreen({}, Modifier.fillMaxSize())
//    }
//}

@Preview(showBackground = true)
@Composable
fun ResultScreenPreview() {
    BookshelfTheme {
        val mockData = List(10) {
            Book(
                it.toString() ,
                VolumeInfo(
                    "Lorem Ipsum - $it",
                    listOf("An author"),
                    ImageLinks("")
                )
            )
        }
        ResultScreen(NavHostController(LocalContext.current),mockData, Modifier.fillMaxSize())
    }
}