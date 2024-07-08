package com.example.finalproject.data.datasource.local.cart.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.finalproject.model.shopping.CartEntity

@Dao
interface CartDao {

    @Insert
    suspend fun addProductToCart(cartEntity: CartEntity)

    @Query("DELETE FROM CartEntity WHERE id == :productId")
    suspend fun removeProductFromCart(productId: Int)

    @Query("SELECT * FROM CartEntity")
    suspend fun getCart(): List<CartEntity>

    @Query("SELECT * FROM CartEntity WHERE id == :productId LIMIT 1")
    suspend fun findCartItem(productId: Int): CartEntity?

    @Query("UPDATE CartEntity SET count = count + 1 WHERE id == :cartItemId")
    suspend fun increaseCartItemCount(cartItemId: Int)

    @Query("UPDATE CartEntity SET count = count - 1 WHERE id == :cartItemId")
    suspend fun decreaseCartItemCount(cartItemId: Int)

    @Query("DELETE FROM CartEntity")
    suspend fun deleteAllItems()
}