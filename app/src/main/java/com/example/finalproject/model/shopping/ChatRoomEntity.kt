package com.example.finalproject.model.shopping

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class ChatDTO(
    val id: Int,
    val from_user_id: String,
    val to_user_id: String,
    val content: String,
    val created_at: String
)

@Immutable
@Serializable
data class ChatListResponse(
    val from_user_id: String,
    val to_user_id: String
)