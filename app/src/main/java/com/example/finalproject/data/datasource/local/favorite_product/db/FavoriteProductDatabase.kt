package com.example.finalproject.data.datasource.local.favorite_product.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.finalproject.model.shopping.BookEntity

//@Database(entities = [BookEntity::class], version = 1, exportSchema = false)
//abstract class FavoriteProductDatabase : RoomDatabase() {
//    abstract fun favoriteProductDao(): FavoriteProductDao
//}

//@Database(
//    version = 2,
//    entities = [BookEntity::class],
//    autoMigrations = [
//        AutoMigration (from = 1, to = 2)
//    ],
//    exportSchema = true
//)
//abstract class FavoriteProductDatabase : RoomDatabase() {
//    abstract fun favoriteProductDao(): FavoriteProductDao
//}
@Database(
    version = 3,
    entities = [BookEntity::class],
    autoMigrations = [
        AutoMigration (from = 2, to = 3)
    ],
    exportSchema = true
)
abstract class FavoriteProductDatabase : RoomDatabase() {
    abstract fun favoriteProductDao(): FavoriteProductDao
}