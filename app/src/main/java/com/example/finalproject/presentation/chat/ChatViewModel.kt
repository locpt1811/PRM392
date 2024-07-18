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
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import com.example.finalproject.domain.repository.AuthRepository
import com.example.finalproject.domain.repository.ProfileRepository
import com.example.finalproject.model.auth.User
import com.example.finalproject.model.shopping.ChatDTO
import com.example.finalproject.presentation.navigation.MainDestinations
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking

@Stable
@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val chatRepository: ChatRepository,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatDTO>>(emptyList())
    val messages: StateFlow<List<ChatDTO>> = _messages

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _otherUserName = MutableStateFlow<String?>(null)
    val otherUserName: StateFlow<String?> = _otherUserName

    private val userId = retrieveCurrentUser()?.uid
    private val otherUserId = savedStateHandle.get<String>(MainDestinations.CHAT_OTHER_USER_ID)

    private var isListening = false

    init {
        viewModelScope.launch {
            fetchMessages()
            fetchOtherUserName()
            listenToMessages()
        }
    }

    private suspend fun fetchOtherUserName() {
        if (otherUserId != null) {
            viewModelScope.launch(ioDispatcher) {
                when (val response = profileRepository.getProfileUsers()) {
                    is Response.Success -> {
                        val otherUserProfile = response.data.firstOrNull { it.id == otherUserId }
                        _otherUserName.value = otherUserProfile?.let {
                            "${it.first_name.orEmpty()} ${it.last_name.orEmpty()}"
                        }
                    }
                    is Response.Error -> _errorMessage.value = "Failed to fetch user profile"
                }
            }
        }
    }
    fun fetchMessages() {
        if (userId != null && otherUserId != null) {
            viewModelScope.launch(ioDispatcher) {
                val userMessagesResponse = chatRepository.getAllChat(userId, otherUserId)
                val otherMessagesResponse = chatRepository.getAllOtherChat(userId, otherUserId)

                if (userMessagesResponse is Response.Success && otherMessagesResponse is Response.Success) {
                    val combinedMessages =
                        (userMessagesResponse.data + otherMessagesResponse.data).sortedBy { it.created_at }
                    _messages.value = combinedMessages
                } else {
                    _errorMessage.value = "Error fetching messages"
                }
            }
        }
    }


    fun sendMessage(content: String) {
        if (userId != null && otherUserId != null) {
            viewModelScope.launch(ioDispatcher) {
                when (val response = chatRepository.sendChatMessage(userId, otherUserId, content)) {
                    is Response.Success -> fetchMessages()
                    is Response.Error -> _errorMessage.value = "Failed to send message"
                }
            }
        }
    }
    fun sendImageMessage(imagePath: ByteArray) {
        if (userId != null && otherUserId != null) {
            viewModelScope.launch(ioDispatcher) {
                when (val response = chatRepository.sendImageMessage(userId, otherUserId, imagePath)) {
                    is Response.Success -> fetchMessages()
                    is Response.Error -> _errorMessage.value = "Failed to send image"
                }
            }
        }
    }

    suspend fun listenToMessages() {
        if (!isListening && userId != null && otherUserId != null) {
            isListening = true
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

