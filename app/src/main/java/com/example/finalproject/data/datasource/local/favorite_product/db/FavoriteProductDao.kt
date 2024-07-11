package com.example.finalproject.data.datasource.local.favorite_product.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.finalproject.model.shopping.BookEntity

@Dao
interface FavoriteProductDao {

    @Insert
    suspend fun addFavoriteProduct(productEntity: BookEntity)

    @Query("SELECT * FROM BookEntity")
    suspend fun getAllFavoriteProducts(): List<BookEntity>

    @Query("SELECT * FROM BookEntity WHERE book_id == :productId LIMIT 1")
    suspend fun findFavoriteProduct(productId: Int): BookEntity?

    @Query("DELETE FROM BookEntity WHERE book_id == :productId")
    suspend fun removeFavoriteProduct(productId: Int)
}