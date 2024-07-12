package com.example.finalproject.presentation.chat
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.common.Response
import com.example.finalproject.domain.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.finalproject.model.shopping.MessageDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.compose.runtime.Stable
import com.example.finalproject.domain.repository.AuthRepository
import com.example.finalproject.model.auth.User
import com.example.finalproject.model.shopping.ChatDTO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking

@Stable
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val authRepository: AuthRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatDTO>>(emptyList())
    val messages: StateFlow<List<ChatDTO>> = _messages

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage
    private val userId = retrieveCurrentUser()?.uid
    fun fetchMessages(otherUserId: String) {
        if (userId != null) {
            viewModelScope.launch(ioDispatcher) {
                val userMessagesResponse = chatRepository.getAllChat(userId, otherUserId)
                val otherMessagesResponse = chatRepository.getAllOtherChat(userId, otherUserId)

                if (userMessagesResponse is Response.Success && otherMessagesResponse is Response.Success) {
                    val combinedMessages = (userMessagesResponse.data + otherMessagesResponse.data).sortedBy { it.created_at }
                    _messages.value = combinedMessages
                } else {
                    _errorMessage.value = "Error fetching messages"
                }
            }
        }
    }


    fun sendMessage(otherUserId: String, content: String) {
        if(userId!=null){
            viewModelScope.launch(ioDispatcher) {
                when (val response = chatRepository.sendChatMessage(userId, otherUserId, content)) {
                    is Response.Success -> fetchMessages(otherUserId)
                    is Response.Error -> _errorMessage.value = "Failed to send message"
                }
            }
        }
    }
    suspend fun listenToMessages(otherUserId: String) {
        if (userId != null) {
            Log.e("ChatVM", "Listening Start")
            chatRepository.listenToMessages(userId, otherUserId) { newMessage ->
                viewModelScope.launch(ioDispatcher) {
                    Log.e("ChatVM", "Listening Working")
                    _messages.value = _messages.value + newMessage
                }
            }
        }
    }

    fun retrieveCurrentUser(): User? {
        return runBlocking {
            authRepository.retreiveCurrentUser()
        }
    }
}

