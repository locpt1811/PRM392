package com.example.finalproject.domain.repository

import com.example.finalproject.common.Response
import com.example.finalproject.model.shopping.BookDTO
import com.example.finalproject.model.shopping.BookEntity
import com.example.finalproject.model.shopping.CartEntity
import com.example.finalproject.model.shopping.CateDTO
import com.example.finalproject.model.shopping.ChatDTO

interface ChatRepository {
    suspend fun listenToMessages(userId: String, otherUserId: String, onNewMessage: (ChatDTO) -> Unit)
    suspend fun unsubscribeFromMessages()

    suspend fun getAllChatUser(userId: String): Response<List<String>>
    suspend fun getAllChat(userId: String, otherUserId: String): Response<List<ChatDTO>>
    suspend fun getAllOtherChat(userId: String, otherUserId: String): Response<List<ChatDTO>>
    suspend fun sendChatMessage(fromUserId: String,toUserId: String, content: String): Response<Unit>
    suspend fun sendImageMessage(fromUserId: String,toUserId: String, content: ByteArray): Response<Unit>
}
