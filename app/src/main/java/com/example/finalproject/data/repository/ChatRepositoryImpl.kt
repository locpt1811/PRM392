package com.example.finalproject.data.repository

import android.util.Log
import com.example.finalproject.R
import com.example.finalproject.common.Response
import com.example.finalproject.domain.repository.ChatRepository
import com.example.finalproject.model.shopping.ChatDTO
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
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.buildJsonObject
import javax.inject.Inject
class ChatRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val supabaseClient: SupabaseClient
) : ChatRepository {
    override suspend fun listenToMessages(userId: String, otherUserId: String, onNewMessage: (ChatDTO) -> Unit) {
            val channel = supabaseClient.realtime.channel("chat")

            channel.postgresChangeFlow<PostgresAction.Insert>(schema = "public") {
                table = "chat"
            }
                .map {
                    Log.e("ChatRepo","Realtime Activate")
                    it.decodeRecord<ChatDTO>() }
//                .filter {
//                    (it.from_user_id == userId && it.to_user_id == otherUserId) ||
//                            (it.from_user_id == otherUserId && it.to_user_id == userId)
//                }
                .collect { newMessage ->
                    Log.e("ChatRepo", "Received new message: $newMessage")
                    onNewMessage(newMessage)
                }

            channel.subscribe()
    }
    override suspend fun sendChatMessage(fromUserId: String,toUserId: String, content: String): Response<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                postgrest.from("chat")
                    .insert(mapOf(
                        "from_user_id" to fromUserId,
                        "to_user_id" to toUserId,
                        "content" to content
                    ))
                Response.Success(Unit)
            } catch (e: Exception) {
                Response.Error(errorMessageId = R.string.error_message_books)
            }
        }
    }
    override suspend fun getAllChatUser(userId: String): Response<List<String>> {
        return withContext(Dispatchers.IO) {
            try {
                val result = postgrest.from("chat")
                    .select() {
                        filter {
                            eq("from_user_id", userId)
                        }
                    }
                    .decodeList<ChatDTO>()
                val toUserIds = result.map { it.to_user_id }
                Response.Success(toUserIds)
            } catch (e: Exception) {
                Response.Error(errorMessageId = R.string.error_message_books)
            }
        }
    }

    override suspend fun getAllChat(userId: String, otherUserId: String): Response<List<ChatDTO>> {
        return withContext(Dispatchers.IO) {
            try {
                val result = postgrest.from("chat")
                    .select() {
                        filter {
                            eq("from_user_id", userId)
                            and { eq("to_user_id", otherUserId) }
                        }
                    }
                    .decodeList<ChatDTO>()
                Response.Success(result)
            } catch (e: Exception) {
                Response.Error(errorMessageId = R.string.error_message_books)
            }
        }
    }
    override suspend fun getAllOtherChat(userId: String, otherUserId: String): Response<List<ChatDTO>> {
        return withContext(Dispatchers.IO) {
            try {
                val result = postgrest.from("chat")
                    .select() {
                        filter {
                            eq("from_user_id", otherUserId)
                            and { eq("to_user_id", userId) }
                        }
                    }
                    .decodeList<ChatDTO>()
                Response.Success(result)
            } catch (e: Exception) {
                Response.Error(errorMessageId = R.string.error_message_books)
            }
        }
    }
}
