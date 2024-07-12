package com.example.finalproject.data.repository

import com.example.finalproject.R
import com.example.finalproject.common.Response
import com.example.finalproject.domain.repository.ChatRepository
import com.example.finalproject.model.shopping.ChatRoomDTO
import com.example.finalproject.model.shopping.ChatRoomParticipantDTO
import com.example.finalproject.model.shopping.MessageDTO
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.decodeRecord
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
class ChatRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val supabaseClient: SupabaseClient
) : ChatRepository {

    override suspend fun fetchChatRooms(userId: String): Response<List<ChatRoomDTO>> {
        return withContext(Dispatchers.IO) {
            try {
                val result = postgrest.from("chat_room_user")
                    .select() {
                        filter {
                            eq("user_id", userId)
                        }
                    }
                    .decodeList<ChatRoomDTO>()
                Response.Success(result)
            } catch (e: Exception) {
                Response.Error(errorMessageId = R.string.error_message_books)
            }
        }
    }

    override suspend fun sendMessage(chatRoomId: String, userId: String, content: String): Response<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                postgrest.from("messages")
                    .insert(mapOf(
                        "chat_room_id" to chatRoomId,
                        "user_id" to userId,
                        "content" to content
                    ))
                Response.Success(Unit)
            } catch (e: Exception) {
                Response.Error(errorMessageId = R.string.error_message_books)
            }
        }
    }

    override fun listenToMessages(chatRoomId: String, onNewMessage: (MessageDTO) -> Unit) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val channel = supabaseClient.channel("messages")

            channel.postgresChangeFlow<PostgresAction.Insert>(schema = "public") {
                table = "messages"
                filter = "chat_room_id=eq.$chatRoomId"
            }
                .map { it.decodeRecord<MessageDTO>() }
                .collect { newMessage ->
                    onNewMessage(newMessage)
                }

            channel.subscribe()
        }
    }
    override suspend fun getChatRoomId(userId1: String, userId2: String): Response<String?> {
        return withContext(Dispatchers.IO) {
            try {
                // Query existing chat rooms where both users are participants
                val result = postgrest.from("chat_room_user")
                    .select() {
                        filter {
                            eq("user_id", userId1)
                            or {
                                eq("user_id", userId2)
                            }
                        }
                    }
                    .decodeList<ChatRoomParticipantDTO>()

                // Group by chat room ID and find a common chat room where both users are participants
                val chatRoomIds = result.groupBy { it.chatRoomId }
                val commonChatRoomId = chatRoomIds.entries.find { it.value.size == 2 }?.key

                if (commonChatRoomId != null) {
                    Response.Success(commonChatRoomId.toString())
                } else {
                    // If no existing chat room found, create a new chat room
                    val newChatRoomId = createNewChatRoom(userId1, userId2)
                    if (newChatRoomId != null) {
                        Response.Success(newChatRoomId.toString())
                    } else {
                        Response.Error(errorMessageId = R.string.error_message_books)
                    }
                }
            } catch (e: Exception) {
                Response.Error(errorMessageId = R.string.error_message_books)
            }
        }
    }


    private suspend fun createNewChatRoom(userId1: String, userId2: String): Int? {
        return try {
            // Insert a new chat room and get its ID
            val insertResult = postgrest.from("chat_room")
                .insert(
                    ChatRoomDTO(
                        id = 0
                    )
                )

            if (insertResult.data.isNotEmpty()) {
                val chatRoomId = insertResult.decodeSingle<Int>()
                if (chatRoomId != null) {
                    // Add both users as participants to the new chat room
                    addParticipantsToChatRoom(chatRoomId, userId1, userId2)
                    chatRoomId
                } else {
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun addParticipantsToChatRoom(chatRoomId: Int, userId1: String, userId2: String) {
        try {
            // Insert participants into chat_room_participants table
            postgrest.from("chat_room_user")
                .insert(
                    listOf(
                        ChatRoomParticipantDTO(chatRoomId = chatRoomId, userId = userId1),
                        ChatRoomParticipantDTO(chatRoomId = chatRoomId, userId = userId2)
                    )
                )
        } catch (e: Exception) {
            throw e
        }
    }


    override suspend fun getAllMessages(chatRoomId: String): Response<List<MessageDTO>> {
        return withContext(Dispatchers.IO) {
            try {
                val result = postgrest.from("chat_messages")
                    .select() {
                        filter {
                            eq("chat_room_id", chatRoomId)
                        }
                    }
                    .decodeList<MessageDTO>()
                Response.Success(result)
            } catch (e: Exception) {
                Response.Error(errorMessageId = R.string.error_message_books)
            }
        }
    }
}
