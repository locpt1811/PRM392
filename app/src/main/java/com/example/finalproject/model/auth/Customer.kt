package com.example.finalproject.model.auth

data class Customer(
    val uid: Long,
    val name: String? = null,
    val email: String,
    val photoUrl: String? = null,
    val emailVerified: Boolean
)