package com.example.finalproject.data.repository

import android.util.Log
import com.example.finalproject.R
import com.example.finalproject.common.Response
import com.example.finalproject.data.datasource.local.cart.CartLocalDataSource
import com.example.finalproject.data.datasource.local.favorite_product.FavoriteProductLocalDatasource
import com.example.finalproject.data.mapper.toBookDTO
import com.example.finalproject.data.mapper.toProductEntity
import com.example.finalproject.model.shopping.BookDTO
import com.example.finalproject.domain.repository.BookRepository
import com.example.finalproject.model.shopping.BookEntity
import com.example.finalproject.model.shopping.CartEntity
import com.example.finalproject.model.shopping.CateDTO
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.RpcMethod
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val favoriteProductLocalDatasource: FavoriteProductLocalDatasource,
    private val cartLocalDataSource: CartLocalDataSource
    ) : BookRepository {
    override suspend fun getCategories(): Response<List<CateDTO>> {
        return withContext(Dispatchers.IO) {
            try {
                val result = postgrest.from("category")
                    .select()
                    .decodeList<CateDTO>()
                Response.Success(result)
            } catch (e: Exception) {
                Response.Error(errorMessageId = R.string.error_message_categories)
            }

        }
    }

    override suspend fun getBooks(): Response<List<BookDTO>> {
        return withContext(Dispatchers.IO) {
            try {
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
    override suspend fun getBooksFullDetail(): Response<List<BookDTO>> {
        return withContext(Dispatchers.IO) {
            try {
                // Fetch data from RPC endpoint and decode to List<BookEntity>
                val result = postgrest.rpc("get_books_with_details")
                    .decodeList<BookEntity>()

                // Map BookEntity to BookDTO using extension function
                val booksDto = result.map { entity ->
                    entity.toBookDTO()
                }

                Response.Success(booksDto)
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
                Response.Error(errorMessageId = R.string.error_message_books_id)
            }
        }
    }

    override suspend fun getBookByIdFullDetail(id: String): Response<BookDTO> {
        return withContext(Dispatchers.IO) {
            try {
                val book = postgrest.rpc("get_books_by_id_with_details?id=${id}", RpcMethod.GET)
                    .decodeSingle<BookEntity>()
                    .toBookDTO()

                Response.Success(book)
            } catch (e: Exception) {
                Response.Error(errorMessageId = R.string.error_message_books_id)
            }
        }
    }


    override suspend fun getAllBookDb(): Response<List<BookEntity>> {
        return withContext(Dispatchers.IO) {
            try {

                val result = postgrest.rpc("get_books_with_details")
                    .decodeList<BookEntity>()

                Response.Success(result)
            } catch (e: Exception) {
                Response.Error(errorMessageId = R.string.error_message_books_db)
            }

        }
    }

    override suspend fun getAllBookDbByTitle(title: String): Response<List<BookEntity>> {
        return withContext(Dispatchers.IO) {
            try {

                val result = postgrest.rpc("get_books_by_title_with_details?book_title=${title}",RpcMethod.GET)
                    .decodeList<BookEntity>()

                Response.Success(result)
            } catch (e: Exception) {
                Log.e("Error", e.toString())
                Response.Error(errorMessageId = R.string.error_message_books_db)
            }

        }
    }

    override suspend fun getAllBookDbByCateName(cateName: String): Response<List<BookEntity>> {
        return withContext(Dispatchers.IO) {
            try {

                val result = postgrest.rpc("get_books_by_category_with_details?category_name=${cateName}",RpcMethod.GET)
                    .decodeList<BookEntity>()

                Response.Success(result)
            } catch (e: Exception) {
                Log.e("Error", e.toString())
                Response.Error(errorMessageId = R.string.error_message_books_db)
            }

        }
    }

    override suspend fun addFavoriteProduct(productEntity: BookEntity): Response<Unit> =
        favoriteProductLocalDatasource.addFavoriteProduct(productEntity)

    override suspend fun getAllFavoriteProducts(): Response<List<BookEntity>> =
        favoriteProductLocalDatasource.getAllFavoriteProducts()

    override suspend fun findFavoriteProduct(productId: Int): Response<BookEntity?> =
        favoriteProductLocalDatasource.findFavoriteProduct(productId)

    override suspend fun removeFavoriteProduct(productId: Int): Response<Unit> =
        favoriteProductLocalDatasource.removeFavoriteProduct(productId)

    override suspend fun addProductToCart(cartEntity: CartEntity): Response<Unit> =
        cartLocalDataSource.addProductToCart(cartEntity)

    override suspend fun removeProductFromCart(productId: Int): Response<Unit> =
        cartLocalDataSource.removeProductFromCart(productId)

    override suspend fun getCart(): Response<List<CartEntity>> =
        cartLocalDataSource.getCart()

    override suspend fun findCartItem(productId: Int): Response<CartEntity?> =
        cartLocalDataSource.findCartItem(productId)

    override suspend fun increaseCartItemCount(cartItemId: Int): Response<Unit> =
        cartLocalDataSource.increaseCartItemCount(cartItemId)

    override suspend fun decreaseCartItemCount(cartItemId: Int): Response<Unit> =
        cartLocalDataSource.decreaseCartItemCount(cartItemId)

    override suspend fun deleteAllCartItems(): Response<Unit> =
        cartLocalDataSource.deleteAllItemsFromCart()

}


