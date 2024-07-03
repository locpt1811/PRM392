package com.example.finalproject.domain.repository

import com.example.finalproject.common.Response
import com.example.finalproject.model.shopping.BookDTO
import com.example.finalproject.model.shopping.BookEntity

interface BookRepository {
    suspend fun getCategories(): Response<List<String>>
    suspend fun getBooks(): Response<List<BookDTO>>
    suspend fun getBookById(id: String): Response<BookDTO>
    suspend fun getAllBookDb(id: String): Response<BookEntity>
}
