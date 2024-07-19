package com.example.finalproject.data.repository

import android.util.Log
import com.example.finalproject.common.helper.PreferenceManager
import com.example.finalproject.data.mapper.toUser
import com.example.finalproject.domain.repository.AuthRepository
import com.example.finalproject.model.auth.User
import com.example.finalproject.model.shopping.UserProfileDTO
import com.example.finalproject.model.shopping.UserProfileInfo
import com.example.finalproject.utils.ACCESS_TOKEN
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.gotrue.user.UserUpdateBuilder
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val auth: Auth,
    private val preferenceManager: PreferenceManager,

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

                val userInfo =  auth.currentUserOrNull()
                Log.d("AuthRepositoryImpl", "UserInfo: $userInfo")
                val user = userInfo?.toUser()
                Log.d("AuthRepositoryImpl", "User: $user")

//                val id = auth.currentUserOrNull()?.id
//                if(id != null) {
//                    try{
//                        val user = postgrest.from("profiles")
//                            .select(){
//                                filter {
//                                    eq("id",id)
//                                }
//                            }
//                            .decodeSingle<UserProfileInfo>()
//                        Log.d("AuthRepositoryImpl", "User retreive: $user")
//                        if(user == null){
//                            val userProfile = UserProfileInfo(id, "first_name", "last_name")
//                            postgrest.from("profiles").insert(userProfile)
//                            Log.d("AuthRepositoryImpl", "New user profile created: $userProfile")
//                        }
//                    }catch (e: Exception) {
//                        Log.d("AuthRepositoryImpl", "Error fetching user profile: ${e.message}")
//                    }
//                }
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

            val id = auth.currentUserOrNull()?.id
            if(id != null) {
                val userProfile = UserProfileInfo(id
                    , "firstName"
                    , "lastName"
                    ,"https://th.bing.com/th/id/OIP.oRoosgD6pPrJW2PXAJ-hBwHaJ4?rs=1&pid=ImgDetMain"
                )
                val result = postgrest.from("profiles").insert(userProfile).decodeSingle<UserProfileDTO>()
                Log.d("AuthRepositoryImpl", "Profile, error: ${result}")
            }

            true
        } catch (e: Exception) {
            Log.d("AuthRepositoryImpl", "Sign up failed, error: ${e.message}")
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

    override suspend fun retreiveCurrentUser(): User? {
        return try{
            val userInfo =  auth.currentUserOrNull()
            val user = userInfo?.toUser() ?: User(emailVerified = true, email = "matkhaula123456@gmail.com",uid = "24e5f0cc-713b-4890-b855-ec54df7a2228")
            user
        }catch (e: Exception){
            null
        }

    }

    override suspend fun sendEmailVerification(): Boolean {
        return try {
//            auth.verifyEmailOtp()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun updateUser(config: UserUpdateBuilder.() -> Unit): UserInfo? {
        return try {
            auth.updateUser(true,"",config)
        } catch (e: Exception) {
            Log.d("AuthRepositoryImpl", "Update user failed, error: ${e.message}")
            null
        }
    }

    override suspend fun  updateUserDisplayName(lastName: String, firstName: String): Boolean {
        return try{
            val id = auth.currentUserOrNull()?.id
            if(id != null){
                val result = postgrest.from("profiles").update({
                    set("first_name", firstName)
                    set("last_name", lastName)
                }) {
                    filter {
                        eq("id",id)
                    }
                }
            }

            true
        }catch (e: Exception){
            Log.d("AuthRepositoryImpl", "Update user failed, error: ${e.message}")
            false
        }
    }
}
