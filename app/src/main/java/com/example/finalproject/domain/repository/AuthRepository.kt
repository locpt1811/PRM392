package com.example.finalproject.domain.repository

import android.util.Log
import com.example.finalproject.model.auth.AuthReq
import com.example.finalproject.model.auth.User
import com.example.finalproject.model.shopping.UserProfileInfo
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.gotrue.user.UserUpdateBuilder

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Boolean
    suspend fun signUp(email: String, password: String): Boolean
    suspend fun signInWithGoogle(): Boolean
    suspend fun logout() : Boolean
    suspend fun sendEmailVerification():Boolean
     fun isLoggedIn(): Boolean

     suspend fun retreiveCurrentUser(): User?

     suspend fun updateUser(config: UserUpdateBuilder.() -> Unit): UserInfo?

    suspend fun  updateUserDisplayName(lastName : String, firstName: String): Boolean

}
