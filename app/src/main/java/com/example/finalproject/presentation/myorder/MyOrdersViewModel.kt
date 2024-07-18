package com.example.finalproject.presentation.myorder

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.common.Response
import com.example.finalproject.domain.repository.AuthRepository
import com.example.finalproject.domain.repository.ChatRepository
import com.example.finalproject.domain.repository.OrderRepository
import com.example.finalproject.domain.repository.ProfileRepository
import com.example.finalproject.model.shopping.ChatDTO
import com.example.finalproject.model.shopping.OrderDTO
import com.example.finalproject.model.shopping.UserProfileDTO
import com.example.finalproject.presentation.chat.ChatListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MyOrdersViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val authRepository: AuthRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _orders = MutableStateFlow<List<OrderDTO>>(emptyList())
    val orders: StateFlow<List<OrderDTO>> = _orders

    init {
        viewModelScope.launch {
            fetchChatUsers()
        }
    }

    private suspend fun fetchChatUsers() {
        viewModelScope.launch(ioDispatcher) {
            val currentUser = authRepository.retreiveCurrentUser()?.uid
            if (currentUser != null) {
                val response = orderRepository.getOrdersByUserId(UUID.fromString(currentUser))
                if (response is Response.Success<*>) {
                    _orders.value = response.data as List<OrderDTO>
                }
            }
        }
    }
}