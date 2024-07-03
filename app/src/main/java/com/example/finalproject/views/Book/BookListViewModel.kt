package com.example.finalproject.views.Book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.model.shopping.Book
import com.example.finalproject.model.shopping.BookDTO
import com.example.finalproject.domain.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor(
    private val bookRepository: BookRepository,
) : ViewModel() {

    private val _productList = MutableStateFlow<List<Book>?>(listOf())
    val productList: Flow<List<Book>?> = _productList


    private val _isLoading = MutableStateFlow(false)
    val isLoading: Flow<Boolean> = _isLoading

    init {
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            val books = bookRepository.getBooks()
            _productList.emit(books?.map { it -> it.asDomainModel() })
        }
    }



    private fun BookDTO.asDomainModel(): Book {
        return Book(
            book_id = this.book_id,
            title = this.title,
            isbn13 = this.isbn13,
            language_id = this.language_id,
            num_pages = this.num_pages,
            publication_date = this.publication_date,
            publisher_id = this.publisher_id
        )
    }

}
