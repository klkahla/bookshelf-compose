package com.example.bookshelf.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookshelf.model.Book

@Composable
fun BookDetail(book: Book, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = book.volumeInfo?.title ?: "No Title", style = MaterialTheme.typography.headlineMedium)
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(book.volumeInfo?.imageLinks?.thumbnail?.replace("http://", "https://"))
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        Text(text = "Authors: ${book.volumeInfo?.authors?.joinToString(", ") ?: "Unknown"}")
    }
}