package com.example.bookshelf

import android.app.Application
import com.example.bookshelf.data.AppContainer
import com.example.bookshelf.data.DefaultAppContainer
import kotlinx.serialization.ExperimentalSerializationApi

class BookshelfApplication : Application() {
    lateinit var container: AppContainer

    @OptIn(ExperimentalSerializationApi::class)
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}