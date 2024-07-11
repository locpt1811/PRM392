package com.example.finalproject.domain.repository

import com.example.finalproject.model.auth.AuthReq
import com.example.finalproject.model.auth.User
import io.github.jan.supabase.gotrue.user.UserInfo

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Boolean
    suspend fun signUp(email: String, password: String): Boolean
    suspend fun signInWithGoogle(): Boolean
    suspend fun logout() : Boolean
    suspend fun sendEmailVerification():Boolean
     fun isLoggedIn(): Boolean

     suspend fun retreiveCurrentUser(): User?
}
