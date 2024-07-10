package com.example.finalproject.model.auth

data class User(
    val uid: String,
    val name: String? = null,
    val email: String,
    val photoUrl: String? = null,
    val emailVerified: Boolean
)