package com.example.finalproject.model.shopping

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
@Immutable
@Serializable
data class ChatRoomDTO(
    val id: Int? = null
)

@Immutable
@Serializable
data class ChatRoomParticipantDTO(
    val user_id: String,
    val chat_room_id: Int
)

@Immutable
@Serializable
data class MessageDTO(
    val id: String,
    val chat_room_id: String,
    val user_id: String,
    val content: String
)

@Immutable
@Serializable
data class ChatDTO(
    val id: Int,
    val from_user_id: String,
    val to_user_id: String,
    val content: String,
    val created_at: String
)