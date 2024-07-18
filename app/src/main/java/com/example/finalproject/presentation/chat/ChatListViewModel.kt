package com.example.finalproject.presentation.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.common.Response
import com.example.finalproject.common.helper.UiText
import com.example.finalproject.domain.repository.AuthRepository
import com.example.finalproject.domain.repository.ChatRepository
import com.example.finalproject.domain.repository.ProfileRepository
import com.example.finalproject.model.auth.User
import com.example.finalproject.model.shopping.BookDTO
import com.example.finalproject.model.shopping.UserProfileDTO
import com.example.finalproject.model.shopping.UserProfileInfoDTO
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
    private val profileRepository: ProfileRepository,
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
            Log.e("ChatListVM", "Got the userid chatlist " + uiState.value.userId)
            if (currentUser != null) {
                Log.e("ChatListVM", "Trying to get the users")

                val chatUserResponse = chatRepository.getAllChatUser(currentUser)
                if (chatUserResponse is Response.Success<*>) {
                    val chatUserIds = chatUserResponse.data as List<String>

                    val profileResponse = profileRepository.getProfileUsers()
                    Log.e("ChatListVM", "Trying to get the profiles")
                    if (profileResponse is Response.Success<*>) {
                        val profiles = profileResponse.data as List<UserProfileDTO>
                        Log.e("ChatListVM", "profiles $profiles")

                        val userProfiles = profiles.filter { profile ->
                            chatUserIds.contains(profile.id)
                        }

                        _uiState.update {
                            it.copy(chatList = userProfiles)
                        }
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
    val chatList: List<UserProfileDTO> = listOf(),
    val userId: String? = null
)