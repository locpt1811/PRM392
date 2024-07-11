package com.example.finalproject.presentation.chat
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
import kotlinx.coroutines.CoroutineDispatcher

@Stable
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _messages = MutableStateFlow<List<MessageDTO>>(emptyList())
    val messages: StateFlow<List<MessageDTO>> = _messages

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun fetchMessages(chatRoomId: String) {
        viewModelScope.launch(ioDispatcher) {
            when (val response = chatRepository.getAllMessages(chatRoomId)) {
                is Response.Success -> _messages.value = response.data
                is Response.Error -> _errorMessage.value = "Failed to load messages"
            }
        }
    }

    fun sendMessage(chatRoomId: String, userId: String, content: String) {
        viewModelScope.launch(ioDispatcher) {
            when (val response = chatRepository.sendMessage(chatRoomId, userId, content)) {
                is Response.Success -> fetchMessages(chatRoomId) // Refresh messages after sending
                is Response.Error -> _errorMessage.value = "Failed to send message"
            }
        }
    }

    fun listenToMessages(chatRoomId: String) {
        chatRepository.listenToMessages(chatRoomId) { newMessage ->
            _messages.value = _messages.value + newMessage
        }
    }
}

