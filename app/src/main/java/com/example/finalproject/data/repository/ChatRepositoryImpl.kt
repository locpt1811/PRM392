package com.example.finalproject.data.repository

import android.util.Log
import com.example.finalproject.R
import com.example.finalproject.common.Response
import com.example.finalproject.di.SupabaseModule_ProvideSupabaseStorageFactory
import com.example.finalproject.domain.repository.ChatRepository
import com.example.finalproject.model.shopping.ChatDTO
import com.example.finalproject.model.shopping.ChatListResponse
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.RpcMethod
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.decodeRecord
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import io.github.jan.supabase.realtime.selectAsFlow
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File
import java.util.UUID
import javax.inject.Inject
class ChatRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val supabaseClient: SupabaseClient,
    private val storage: Storage
) : ChatRepository {

    private var channel: RealtimeChannel? = null
    override suspend fun listenToMessages(userId: String, otherUserId: String, onNewMessage: (ChatDTO) -> Unit) {

        channel = supabaseClient.realtime.channel("public:chat"+otherUserId)

        val dataFlow = channel!!.postgresChangeFlow<PostgresAction.Insert>(schema = "public") {
            table = "chat"
        }
            .map {
                it.decodeRecord<ChatDTO>() }

        channel!!.subscribe(blockUntilSubscribed = true)

        // Collect the data flow
        dataFlow.collect { newMessage ->
            val isBetweenUsers = (newMessage.to_user_id == otherUserId && newMessage.from_user_id == userId) ||
                    (newMessage.to_user_id == userId && newMessage.from_user_id == otherUserId)
            Log.e("ChatRepo","Got a new m "+newMessage+" but is it between? "+isBetweenUsers)
            if (isBetweenUsers) {
                onNewMessage(newMessage)
            }
        }
    }

    override suspend fun unsubscribeFromMessages() {
        Log.e("ChatRepo","U N S U B")
        channel?.unsubscribe()
        channel = null
    }


    override suspend fun sendChatMessage(fromUserId: String, toUserId: String, content: String): Response<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                postgrest.from("chat")
                    .insert(mapOf(
                        "from_user_id" to fromUserId,
                        "to_user_id" to toUserId,
                        "content" to content,
                        "is_image" to "false"
                    ))
                Response.Success(Unit)
            } catch (e: Exception) {
                Log.e("ChatRepo", "Chat sent exception: ${e.message}")
                Response.Error(errorMessageId = R.string.error_message_books)
            }
        }
    }

    override suspend fun sendImageMessage(fromUserId: String, toUserId: String, imagePath: ByteArray): Response<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                // Upload the image to Supabase storage
                val imageUrl = uploadImage(imagePath)
                Log.e("ChatRepo", "ImageUrl: ${imageUrl}")
                if (imageUrl != null) {
                    postgrest.from("chat")
                        .insert(mapOf(
                            "from_user_id" to fromUserId,
                            "to_user_id" to toUserId,
                            "content" to imageUrl,
                            "is_image" to "true"
                        ))
                    Response.Success(Unit)
                } else {
                    Response.Error(errorMessageId = R.string.error_message_books)
                }
            } catch (e: Exception) {
                Log.e("ChatRepo", "Image sent exception: ${e.message}")
                Response.Error(errorMessageId = R.string.error_message_books)
            }
        }
    }
    private suspend fun uploadImage(imageBytes: ByteArray): String? {
        return try {
            Log.e("ChatRepo", "try to upload image")
            val fileName = "${UUID.randomUUID()}.jpg"
            val response = storage.from("chat-images").upload(fileName, imageBytes)
            val filePath = response.substringAfterLast('/')
            val imageUrl = storage.from("chat-images").publicUrl(filePath)

            Log.e("ChatRepo", "Image uploaded to: $imageUrl")
            imageUrl
        } catch (e: Exception) {
            Log.e("ChatRepo", "Image upload exception: ${e.message}")
            null
        }
    }


    override suspend fun getAllChatUser(userId: String): Response<List<String>> {
        return withContext(Dispatchers.IO) {
            try {
                val userUuid = UUID.fromString(userId)

                val result = postgrest
                    .rpc("get_chat_list_by_user_id?arg_user_id=${userUuid}",
                        RpcMethod.GET)
                    .decodeList<ChatListResponse>()

                Log.d("ChatRepo", "User Ids result $result")
                val userIds = result.flatMap { listOf(it.from_user_id, it.to_user_id) }
                    .distinct()
                    .filter { it != userId }
                    //result.map { it.to_user_id}.distinct()


                Log.d("ChatRepo", "User Ids List $userIds")
                Response.Success(userIds)
            } catch (e: Exception) {
                Log.e("ChatRepo",e.toString())
                Response.Error(errorMessageId = R.string.error_message_books)
            }
        }
    }

    override suspend fun getAllChat(userId: String, otherUserId: String): Response<List<ChatDTO>> {
        return withContext(Dispatchers.IO) {
            try {
                val result = postgrest.from("chat")
                    .select {
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
                    .select {
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
