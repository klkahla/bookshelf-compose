package com.example.bookshelf.model
import kotlinx.serialization.Serializable

@Serializable
data class Bookshelf(
    val items: List<Book> = listOf()
)

@Serializable
data class Book(
    val id: String = "",
    val volumeInfo: VolumeInfo? = null
)

@Serializable
data class VolumeInfo (
    val title: String = "",
    val authors: List<String> = listOf(),
    val imageLinks: ImageLinks? = null
)

@Serializable
data class ImageLinks (
    val thumbnail: String = ""
)