package com.example.finalproject.domain.repository

import com.example.finalproject.common.Response
import com.example.finalproject.model.shopping.BookDTO
import com.example.finalproject.model.shopping.BookEntity
import com.example.finalproject.model.shopping.CartEntity
import com.example.finalproject.model.shopping.CateDTO
import com.example.finalproject.model.shopping.ChatRoomDTO
import com.example.finalproject.model.shopping.MessageDTO

interface ChatRepository {
    suspend fun fetchChatRooms(userId: String): Response<List<ChatRoomDTO>>
    suspend fun sendMessage(chatRoomId: String, userId: String, content: String): Response<Unit>
    fun listenToMessages(chatRoomId: String, onNewMessage: (MessageDTO) -> Unit)
    suspend fun getChatRoomId(userId1: String, userId2: String): Response<String?>
    suspend fun getAllMessages(chatRoomId: String): Response<List<MessageDTO>>
}
