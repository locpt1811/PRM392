package com.example.finalproject.di

import android.content.Context
import com.example.finalproject.common.helper.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePreferenceManager(@ApplicationContext context: Context): PreferenceManager =
        PreferenceManager(context)

    @Provides
    @Singleton
    fun provideDispatcherIO(): CoroutineDispatcher = Dispatchers.IO
}