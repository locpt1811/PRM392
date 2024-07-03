package com.example.finalproject.data.repository

import com.example.finalproject.R
import com.example.finalproject.common.Response
import com.example.finalproject.model.shopping.BookDTO
import com.example.finalproject.domain.repository.BookRepository
import com.example.finalproject.model.shopping.BookEntity
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    ) : BookRepository {
    override suspend fun getCategories(): Response<List<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun getBooks(): Response<List<BookDTO>> {
        return withContext(Dispatchers.IO) {
            try {
                val result = postgrest.from("books")
                    .select().decodeList<BookDTO>()
                Response.Success(result)
            } catch (e: Exception) {
                Response.Error<List<BookDTO>>(errorMessageId = R.string.error_message_books)
            }
        }
    }


    override suspend fun getBookById(id: String): Response<BookDTO> {
        return withContext(Dispatchers.IO) {
            try {
                val book = postgrest.from("books")
                    .select {
                        filter {
                            eq("book_id", id)
                        }
                    }
                    .decodeSingle<BookDTO>()

                Response.Success(book)
            } catch (e: Exception) {
                Response.Error(404)
            }
        }
    }


    override suspend fun getAllBookDb(id: String): Response<BookEntity> {
        TODO("Not yet implemented")
    }

}


