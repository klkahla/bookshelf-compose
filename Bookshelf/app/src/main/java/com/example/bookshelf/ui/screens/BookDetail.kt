package com.example.bookshelf.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookshelf.model.Book
import com.example.bookshelf.model.ImageLinks
import com.example.bookshelf.model.VolumeInfo
import com.example.bookshelf.ui.theme.BookshelfTheme

@Composable
fun BookDetail(book: Book, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Enable scrolling
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Book Title
        Text(
            text = book.volumeInfo?.title ?: "No Title",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 8.dp)
            .fillMaxWidth()
        )

        // Book Subtitle (if available)
        book.volumeInfo?.subtitle?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
            )
        }

        // Book Cover Image
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(book.volumeInfo?.imageLinks?.thumbnail?.replace("http://", "https://"))
                .crossfade(true)
                .build(),
            contentDescription = book.volumeInfo?.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .padding(16.dp)
                .clip(MaterialTheme.shapes.medium) // Apply rounded corners
        )

        // Authors
        Text(
            text = "Authors: ${book.volumeInfo?.authors?.joinToString(", ") ?: "Unknown"}",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Publisher and Published Date
        book.volumeInfo?.publisher?.let { publisher ->
            book.volumeInfo.publishedDate?.let { publishedDate ->
                Text(
                    text = "Published by $publisher on $publishedDate",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        // Book Description
        book.volumeInfo?.description?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        // Purchase Button
        book.volumeInfo?.canonicalVolumeLink?.let { purchaseLink ->
            Button(
                onClick = { /* Open purchase link */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = "Buy Now")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookDetailPreview() {
    BookshelfTheme {
        val mockData =
            Book(
                it.toString() ,
                VolumeInfo(
                    "Lorem Ipsum - $it",
                    listOf("An author"),
                    ImageLinks("")
                )
            )

        BookDetail(mockData, Modifier.fillMaxSize())
    }
}