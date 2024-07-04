package com.example.finalproject.domain.repository

import com.example.finalproject.model.auth.AuthReq

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Boolean
    suspend fun signUp(email: String, password: String): Boolean
    suspend fun signInWithGoogle(): Boolean
    suspend fun logout() : Boolean
}
