package com.example.finalproject.data.repository

import android.util.Log
import com.example.finalproject.common.helper.PreferenceManager
import com.example.finalproject.domain.repository.AuthRepository
import com.example.finalproject.utils.ACCESS_TOKEN
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.Email
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(

    private val auth: Auth,
    private val preferenceManager: PreferenceManager
) : AuthRepository {
    override suspend fun signIn(email: String, password: String): Boolean {
        return try {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            auth.currentAccessTokenOrNull()?.let {
                preferenceManager.saveData(ACCESS_TOKEN, it)
                Log.d("AuthRepositoryImpl", "Sign in successful, access token: $it")
            }
            true
        } catch (e: Exception) {
            Log.d("AuthRepositoryImpl", "Sign in failed, error: ${e.message}")
            false
        }
    }

    override suspend fun signUp(email: String, password: String): Boolean {
        return try {
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            auth.currentAccessTokenOrNull()?.let { preferenceManager.saveData(ACCESS_TOKEN, it) }
            Log.d("aaaa", preferenceManager.getData(ACCESS_TOKEN, "").toString())
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun signInWithGoogle(): Boolean {
        return try {
            auth.signInWith(Google)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun logout() : Boolean{
        return try {
            auth.signOut()
            preferenceManager.removeData(ACCESS_TOKEN)
            true
        } catch (e: Exception) {
            false
        }
    }

    override  fun isLoggedIn(): Boolean {
        val token = preferenceManager.getData(ACCESS_TOKEN, "")
        return token.isNotEmpty()
    }
}
