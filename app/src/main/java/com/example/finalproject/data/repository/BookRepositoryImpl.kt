package com.example.finalproject.data.repository

import com.example.finalproject.R
import com.example.finalproject.common.Response
import com.example.finalproject.model.shopping.BookDTO
import com.example.finalproject.domain.repository.BookRepository
import com.example.finalproject.model.shopping.BookEntity
import com.example.finalproject.model.shopping.CateDTO
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    ) : BookRepository {
    override suspend fun getCategories(): Response<List<CateDTO>> {
        return withContext(Dispatchers.IO) {
            try {
                val result = postgrest.from("category")
                    .select()
                    .decodeList<CateDTO>()
                Response.Success(result)
            } catch (e: Exception) {
                Response.Error<List<CateDTO>>(errorMessageId = R.string.error_message_categories)
            }

        }
    }

    override suspend fun getBooks(): Response<List<BookDTO>> {
        return withContext(Dispatchers.IO) {
            try {
//                val columns = Columns.raw("""
//                                            book_id,
//                                            title,
//                                            isbn13,
//                                            language_id,
//                                            num_pages,
//                                            publisher_id
//                                            image_url,
//                                            description,
//                                            rating,
//                                            category_id,
//                                            price,
//
//                                            book_language(
//                                              language_code,
//                                              language_name
//                                            ),
//
//                                            publisher(
//                                              publisher_name
//                                            )
//
//                                            category (
//                                              category_name
//                                            )
//                                        """.trimIndent())
//                val result = postgrest
//                    .from("book")
//                    .select(columns = columns)
//                    .decodeList<BookDTO>()

                val result = postgrest
                    .from("book")
                    .select()
                    .decodeList<BookDTO>()

                Response.Success(result)
            } catch (e: Exception) {
                Response.Error(errorMessageId = R.string.error_message_books)
            }

        }
    }


    override suspend fun getBookById(id: String): Response<BookDTO> {
        return withContext(Dispatchers.IO) {
            try {
                val book = postgrest.from("book")
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


