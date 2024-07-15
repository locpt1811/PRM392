package com.example.finalproject.di

import com.example.finalproject.common.helper.PreferenceManager
import com.example.finalproject.data.datasource.local.cart.CartLocalDataSource
import com.example.finalproject.data.datasource.local.favorite_product.FavoriteProductLocalDatasource
import com.example.finalproject.data.repository.AuthRepositoryImpl
import com.example.finalproject.data.repository.BookRepositoryImpl
import com.example.finalproject.data.repository.ChatRepositoryImpl
import com.example.finalproject.domain.repository.AuthRepository
import com.example.finalproject.domain.repository.BookRepository
import com.example.finalproject.domain.repository.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import okhttp3.OkHttpClient
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        postgrest: Postgrest,
        auth: Auth,
        preferenceManager: PreferenceManager
    ): AuthRepository {
        return AuthRepositoryImpl(postgrest ,auth, preferenceManager)
    }

    @Provides
    @Singleton
    fun provideBookRepository(
        postgrest: Postgrest,
        favoriteProductLocalDatasource: FavoriteProductLocalDatasource,
        cartLocalDataSource: CartLocalDataSource
    ): BookRepository {
        return BookRepositoryImpl(postgrest, favoriteProductLocalDatasource, cartLocalDataSource)
    }
    @Provides
    @Singleton
    fun provideChatRepository(
        postgrest: Postgrest,
        supabaseClient: SupabaseClient
    ): ChatRepository {
        return ChatRepositoryImpl(postgrest, supabaseClient)
    }
}