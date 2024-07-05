package com.example.finalproject.di

import com.example.finalproject.data.datasource.local.cart.CartLocalDataSource
import com.example.finalproject.data.datasource.local.cart.CartLocalDataSourceImpl
import com.example.finalproject.data.datasource.local.cart.db.CartDao
import com.example.finalproject.data.datasource.local.favorite_product.FavoriteLocalDatasourceImpl
import com.example.finalproject.data.datasource.local.favorite_product.FavoriteProductLocalDatasource
import com.example.finalproject.data.datasource.local.favorite_product.db.FavoriteProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideFavoriteProductLocalDataSource(favoriteProductDao: FavoriteProductDao): FavoriteProductLocalDatasource {
        return FavoriteLocalDatasourceImpl(favoriteProductDao)
    }

    @Provides
    @Singleton
    fun provideCartLocalDataSource(cartDao: CartDao): CartLocalDataSource {
        return CartLocalDataSourceImpl(cartDao)
    }
}