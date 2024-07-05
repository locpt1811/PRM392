package com.example.finalproject.di

import android.content.Context
import androidx.room.Room
import com.example.finalproject.data.datasource.local.cart.db.CartDao
import com.example.finalproject.data.datasource.local.cart.db.CartDatabase
import com.example.finalproject.data.datasource.local.favorite_product.db.FavoriteProductDao
import com.example.finalproject.data.datasource.local.favorite_product.db.FavoriteProductDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideFavoriteProductDb(@ApplicationContext context: Context): FavoriteProductDatabase {
        return Room.databaseBuilder(
            context,
            FavoriteProductDatabase::class.java,
            "favorite_product_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteProductDao(db: FavoriteProductDatabase): FavoriteProductDao {
        return db.favoriteProductDao()
    }

    @Provides
    @Singleton
    fun provideCartDb(@ApplicationContext context: Context): CartDatabase {
        return Room.databaseBuilder(
            context,
            CartDatabase::class.java,
            "cart_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCardDao(db: CartDatabase): CartDao {
        return db.cartDao()
    }
}