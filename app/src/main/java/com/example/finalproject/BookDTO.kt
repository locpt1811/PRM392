package com.example.finalproject

import kotlinx.serialization.Serializable

@Serializable
data class BookDTO (
    val book_id: Int,
    val title: String,
    val isbn13: String,
    val language_id: Int,
    val num_pages: Int,
    val publication_date: String,
    val publisher_id: Int
)

data class Book(
    val book_id: Int,
    val title: String,
    val isbn13: String,
    val language_id: Int,
    val num_pages: Int,
    val publication_date: String,
    val publisher_id: Int
)