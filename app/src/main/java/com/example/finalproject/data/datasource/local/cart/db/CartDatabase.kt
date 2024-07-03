package com.example.finalproject.data.datasource.local.cart.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.finalproject.model.shopping.CartEntity

@Database(entities = [CartEntity::class], version = 1)
abstract class CartDatabase : RoomDatabase() {

    abstract fun cartDao(): CartDao
}