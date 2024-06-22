package com.example.finalproject.views.Book

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.Book
import com.example.finalproject.BookDTO
import com.example.finalproject.BookDetailsDestination
import com.example.finalproject.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    private val productRepository: BookRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _product = MutableStateFlow<Book?>(null)
    val product: Flow<Book?> = _product

    private val _name = MutableStateFlow("")
    val name: Flow<String> = _name

    private val _price = MutableStateFlow(0.0)
    val price: Flow<Double> = _price

    private val _imageUrl = MutableStateFlow("")
    val imageUrl: Flow<String> = _imageUrl

    init {
        val productId = savedStateHandle.get<String>(BookDetailsDestination.bookId)
        productId?.let {
            getProduct(bookId = it)
        }
    }

    private fun getProduct(bookId: String) {
        viewModelScope.launch {
            val result = productRepository.getBook(bookId).asDomainModel()
            _product.emit(result)
//            _name.emit(result.name)
//            _price.emit(result.price)
        }
    }

//    fun onNameChange(name: String) {
//        _name.value = name
//    }
//
//    fun onPriceChange(price: Double) {
//        _price.value = price
//    }
//
//    fun onSaveProduct(image: ByteArray) {
//        viewModelScope.launch {
//            productRepository.updateProduct(
//                id = _product.value?.id,
//                price = _price.value,
//                name = _name.value,
//                imageFile = image,
//                imageName = "image_${_product.value.id}",
//            )
//        }
//    }
//
//    fun onImageChange(url: String) {
//        _imageUrl.value = url
//    }


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
