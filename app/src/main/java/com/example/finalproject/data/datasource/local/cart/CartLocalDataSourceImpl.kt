package com.example.finalproject.data.datasource.local.cart

import com.example.finalproject.common.Response
import com.example.finalproject.common.caller.dbCall
import com.example.finalproject.data.datasource.local.cart.db.CartDao
import com.example.finalproject.model.shopping.CartEntity
import javax.inject.Inject

class CartLocalDataSourceImpl @Inject constructor(
    private val dao: CartDao
) : CartLocalDataSource {
    override suspend fun addProductToCart(cartEntity: CartEntity): Response<Unit> {
        return dbCall { dao.addProductToCart(cartEntity) }
    }

    override suspend fun removeProductFromCart(productId: Int): Response<Unit> {
        return dbCall { dao.removeProductFromCart(productId) }
    }

    override suspend fun getCart(): Response<List<CartEntity>> {
        return dbCall { dao.getCart() }
    }

    override suspend fun findCartItem(productId: Int): Response<CartEntity?> {
        return dbCall { dao.findCartItem(productId) }
    }

    override suspend fun increaseCartItemCount(cartItemId: Int): Response<Unit> {
        return dbCall { dao.increaseCartItemCount(cartItemId) }
    }

    override suspend fun decreaseCartItemCount(cartItemId: Int): Response<Unit> {
        return dbCall { dao.decreaseCartItemCount(cartItemId) }
    }

    override suspend fun deleteAllItemsFromCart(): Response<Unit> {
        return dbCall { dao.deleteAllItems() }
    }
}