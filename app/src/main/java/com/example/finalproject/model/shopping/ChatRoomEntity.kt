package com.example.finalproject.model.shopping

data class ChatRoomDTO(
    val id: String,
    val name: String,
    val isGroup: Boolean,
    val createdAt: String
)

data class ChatRoomParticipantDTO(
    val userId: String,
    val chatRoomId: String,
    val joinedAt: String
)

data class MessageDTO(
    val id: String,
    val chatRoomId: String,
    val userId: String,
    val content: String,
    val createdAt: String
)