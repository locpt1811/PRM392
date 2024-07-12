package com.example.finalproject.model.shopping

data class ChatRoomDTO(
    val id: Int
)

data class ChatRoomParticipantDTO(
    val userId: String,
    val chatRoomId: Int
)

data class MessageDTO(
    val id: String,
    val chatRoomId: String,
    val userId: String,
    val content: String,
    val createdAt: String
)