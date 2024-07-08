package com.example.finalproject.data.datasource.local.favorite_product.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.finalproject.model.shopping.BookEntity

@Database(entities = [BookEntity::class], version = 1, exportSchema = false)
abstract class FavoriteProductDatabase : RoomDatabase() {
    abstract fun favoriteProductDao(): FavoriteProductDao
}