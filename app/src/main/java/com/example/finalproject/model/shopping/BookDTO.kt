package com.example.finalproject.model.shopping

import androidx.compose.runtime.Immutable
import java.util.Date

@Immutable
data class BookDTO(
    val book_id: Int,
    val title: String? = null,
    val isbn13: String? = null,
    val num_pages: Int? = null,
    val publication_date: Date? = null,
    val image_url: String? = null,
    val description: String? = null,
    val rating: Double? = null,
    val price: Double? = null,

    val book_language: BookLanguage? = null,
    val category: Category? = null,
    val publisher: Publisher? = null
)

@Immutable
data class BookLanguage(
    val language_id: Int? = null,
    val language_code: String? = null,
    val language_name: String? = null
)

@Immutable
data class Category(
    val category_id: Int? = null,
    val category_name: String? = null
)

@Immutable
data class Publisher(
    val publisher_id: Int? = null,
    val publisher_name: String? = null
)
