package com.example.finalproject.domain.repository

import com.example.finalproject.common.Response
import com.example.finalproject.model.shopping.BookDTO
import com.example.finalproject.model.shopping.BookEntity
import com.example.finalproject.model.shopping.CartEntity
import com.example.finalproject.model.shopping.CateDTO

interface BookRepository {
    suspend fun getCategories(): Response<List<CateDTO>>
    suspend fun getBooks(): Response<List<BookDTO>>
    suspend fun getBooksFullDetail(): Response<List<BookDTO>>
    suspend fun getBookById(id: String): Response<BookDTO>
    suspend fun getBookByIdFullDetail(id: String): Response<BookDTO>
    suspend fun getAllBookDb(): Response<List<BookEntity>>
    suspend fun getAllBookDbByTitle(title: String): Response<List<BookEntity>>
    suspend fun addFavoriteProduct(productEntity: BookEntity): Response<Unit>

    suspend fun getAllFavoriteProducts(): Response<List<BookEntity>>

    suspend fun findFavoriteProduct(productId: Int): Response<BookEntity?>

    suspend fun removeFavoriteProduct(productId: Int): Response<Unit>

    suspend fun addProductToCart(cartEntity: CartEntity): Response<Unit>

    suspend fun removeProductFromCart(productId: Int): Response<Unit>

    suspend fun getCart(): Response<List<CartEntity>>

    suspend fun findCartItem(productId: Int): Response<CartEntity?>

    suspend fun increaseCartItemCount(cartItemId: Int): Response<Unit>

    suspend fun decreaseCartItemCount(cartItemId: Int): Response<Unit>

    suspend fun deleteAllCartItems(): Response<Unit>
}
