package com.example.finalproject.presentation.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.common.Response
import com.example.finalproject.common.helper.UiText
import com.example.finalproject.domain.repository.AuthRepository
import com.example.finalproject.domain.repository.ChatRepository
import com.example.finalproject.model.auth.User
import com.example.finalproject.model.shopping.BookDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val authRepository: AuthRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatListUiState())
    val uiState: StateFlow<ChatListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            retrieveUserId()
            fetchChatUsers()
        }
    }

    private suspend fun fetchChatUsers() {
        viewModelScope.launch(ioDispatcher) {
            val currentUser = uiState.value.userId
            Log.e("ChatListVM","Got the userid chatlist "+uiState.value.userId)
            if (currentUser != null) {
                Log.e("ChatListVM","Try get the users")

                val response = chatRepository.getAllChatUser(currentUser)
                if (response is Response.Success<*>) {
                    _uiState.update {
                        it.copy(chatList = response.data as List<String>)
                    }
                }
            }
        }
    }

    private suspend fun retrieveUserId() {
            val currentUserId = authRepository.retreiveCurrentUser()?.uid
            _uiState.update {
                it.copy(
                    userId = currentUserId
                )
            }
    }
}

data class ChatListUiState(
    val chatList: List<String> = listOf(),
    val userId: String? = null
)