package com.example.finalproject.data.datasource.local.favorite_product

import com.example.finalproject.common.Response
import com.example.finalproject.common.caller.dbCall
import com.example.finalproject.data.datasource.local.favorite_product.db.FavoriteProductDao
import com.example.finalproject.model.shopping.BookEntity
import javax.inject.Inject

class FavoriteLocalDatasourceImpl @Inject constructor(
    private val favoriteProductDao: FavoriteProductDao
) : FavoriteProductLocalDatasource {

    override suspend fun addFavoriteProduct(productEntity: BookEntity): Response<Unit> {
        return dbCall { favoriteProductDao.addFavoriteProduct(productEntity) }
    }

    override suspend fun getAllFavoriteProducts(): Response<List<BookEntity>> {
        return dbCall { favoriteProductDao.getAllFavoriteProducts() }
    }

    override suspend fun findFavoriteProduct(productId: Int): Response<BookEntity?> {
        return dbCall { favoriteProductDao.findFavoriteProduct(productId) }
    }

    override suspend fun removeFavoriteProduct(productId: Int): Response<Unit> {
        return dbCall { favoriteProductDao.removeFavoriteProduct(productId) }
    }
}