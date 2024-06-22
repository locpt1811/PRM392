package com.example.finalproject.repository

import com.example.finalproject.Book
import com.example.finalproject.BookDTO

interface BookRepository {
    suspend fun createBook(book: Book): Boolean
    suspend fun getBooks(): List<BookDTO>?
    suspend fun getBook(id: String): BookDTO
    suspend fun deleteBoook(id: String)
    suspend fun updateBook(
        book_id: Int,
        title: String,
        isbn13: String,
        language_id: Int,
        num_pages: Int,
        publication_date: String,
        publisher_id: Int
    )
}
