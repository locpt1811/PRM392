    package com.example.finalproject.repository

    import com.example.finalproject.Book
    import com.example.finalproject.BookDTO
    import io.github.jan.supabase.postgrest.Postgrest
    import io.github.jan.supabase.storage.Storage
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.withContext
    import javax.inject.Inject

    class BookRepositoryImpl @Inject constructor(
        private val postgrest: Postgrest,
        private val storage: Storage,

        ) : BookRepository {
        override suspend fun createBook(book: Book): Boolean {
            TODO("Not yet implemented")
        }

        override suspend fun getBooks(): List<BookDTO>? {
            return withContext(Dispatchers.IO) {
                val result = postgrest.from("books")
                    .select().decodeList<BookDTO>()
                result
            }
        }

        override suspend fun getBook(id: String): BookDTO {
            return withContext(Dispatchers.IO) {
                postgrest.from("books").select {
                    filter {
                        eq("book_id", id)
                    }
                }.decodeSingle<BookDTO>()
            }
        }

        override suspend fun deleteBoook(id: String) {
            TODO("Not yet implemented")
        }

        override suspend fun updateBook(
            book_id: Int,
            title: String,
            isbn13: String,
            language_id: Int,
            num_pages: Int,
            publication_date: String,
            publisher_id: Int
        ) {
            TODO("Not yet implemented")
        }
    }


