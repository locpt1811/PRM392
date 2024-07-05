package com.example.finalproject.data.datasource.local.favorite_product

import com.example.finalproject.common.Response
import com.example.finalproject.model.shopping.BookEntity


interface FavoriteProductLocalDatasource {

    suspend fun addFavoriteProduct(productEntity: BookEntity): Response<Unit>

    suspend fun getAllFavoriteProducts(): Response<List<BookEntity>>

    suspend fun findFavoriteProduct(productId: Int): Response<BookEntity?>

    suspend fun removeFavoriteProduct(productId: Int): Response<Unit>
}