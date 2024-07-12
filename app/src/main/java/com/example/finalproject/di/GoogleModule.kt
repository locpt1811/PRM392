package com.example.finalproject.di

import android.content.Context
import com.example.finalproject.BuildConfig
import com.example.finalproject.data.repository.GoogleRepositoryImpl
import com.example.finalproject.domain.repository.GoogleRepository
import com.example.finalproject.utils.PaymentsUtil
import com.google.android.gms.wallet.PaymentsClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.FlowType
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GoogleModule {

    @Provides
    @Singleton
    fun provideGoogleRepository(paymentsClient: PaymentsClient): GoogleRepository =
        GoogleRepositoryImpl(paymentsClient)
    @Provides
    @Singleton
    fun provideGoogleClient(@ApplicationContext context: Context): PaymentsClient =
        PaymentsUtil.createPaymentsClient(context)

}
