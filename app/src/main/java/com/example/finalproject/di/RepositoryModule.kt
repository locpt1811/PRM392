package com.example.finalproject.di

import com.example.finalproject.common.helper.PreferenceManager
import com.example.finalproject.data.repository.AuthRepositoryImpl
import com.example.finalproject.data.repository.BookRepositoryImpl
import com.example.finalproject.domain.repository.AuthRepository
import com.example.finalproject.domain.repository.BookRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: Auth, // Provide the Auth dependency
        preferenceManager: PreferenceManager // Provide the PreferenceManager dependency
    ): AuthRepository {
        return AuthRepositoryImpl(auth, preferenceManager)
    }

    @Provides
    @Singleton
    fun provideBookRepository(postgrest: Postgrest): BookRepository {
        return BookRepositoryImpl(postgrest)
    }
}