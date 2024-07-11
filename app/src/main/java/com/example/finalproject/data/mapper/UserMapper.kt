package com.example.finalproject.data.mapper

import com.example.finalproject.model.auth.User
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.serialization.json.jsonPrimitive

fun UserInfo.toUser(): User {
    return User(
        uid = this.id,
        name = this.email, // Use email as name
        email = this.email ?: "",
        photoUrl = "11122233", // Hardcoded image URL
        emailVerified = this.emailConfirmedAt != null
    )
}