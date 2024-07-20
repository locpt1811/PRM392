package com.example.finalproject.presentation.manageorder

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.common.Response
import com.example.finalproject.common.helper.PreferenceManager
import com.example.finalproject.domain.repository.AuthRepository
import com.example.finalproject.domain.repository.OrderRepository
import com.example.finalproject.domain.repository.ProfileRepository
import com.example.finalproject.model.shopping.OrderDTO
import com.example.finalproject.model.shopping.OrderStatusDTO
import com.example.finalproject.model.shopping.UserProfileDTO
import com.example.finalproject.utils.ACCESS_TOKEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
@HiltViewModel
class ManageOrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val profileRepository: ProfileRepository,
    private val ioDispatcher: CoroutineDispatcher,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _orders = MutableStateFlow<List<OrderDTO>>(emptyList())
    val orders: StateFlow<List<OrderDTO>> = _orders

    private val _statuses = MutableStateFlow<List<OrderStatusDTO>>(emptyList())
    val statuses: StateFlow<List<OrderStatusDTO>> = _statuses

    private val _userMessages = MutableStateFlow<List<String>>(emptyList())
    val userMessages: StateFlow<List<String>> = _userMessages
    init {
        viewModelScope.launch {
            fetchOrderStatuses()
            fetchOrders()
        }
    }

    private suspend fun fetchOrders() {
        viewModelScope.launch(ioDispatcher) {
            val response = orderRepository.getOrders()
            if (response is Response.Success<*>) {
                val sortedOrders = (response.data as List<OrderDTO>).sortedByDescending { it.created_at }

                val userProfilesResponse = profileRepository.getProfileUsers()
                if (userProfilesResponse is Response.Success<*>) {
                    val userProfiles = userProfilesResponse.data as List<UserProfileDTO>
                    val userMap = userProfiles.associateBy(
                        keySelector = { it.id },
                        valueTransform = { "${it.first_name.orEmpty()} ${it.last_name.orEmpty()}" }
                    )

                    val updatedOrders = sortedOrders.map { order ->
                        order.copy(
                            user_id = userMap[order.user_id] ?: order.user_id
                        )
                    }

                    _orders.value = updatedOrders
                } else {
                    _orders.value = sortedOrders
                }
            }
        }
    }

    private suspend fun fetchOrderStatuses() {
        viewModelScope.launch(ioDispatcher) {
            val response = orderRepository.getOrderStatuses()
            if (response is Response.Success<*>) {
                _statuses.value = response.data as List<OrderStatusDTO>
            }
        }
    }

    fun fetchCurrentUser(): Boolean {
        return preferenceManager.getData(ACCESS_TOKEN, "").isEmpty()
    }

    fun updateOrderStatus(orderId: Int, statusId: Int) {
        viewModelScope.launch(ioDispatcher) {
            val response = orderRepository.updateOrderStatus(orderId, statusId)
            if (response is Response.Success<*>) {
                _userMessages.value = listOf("Successfully updated status!")
                fetchOrders()
            } else {
            }
        }
    }
    fun consumedUserMessages() {
        _userMessages.value = listOf()
    }
}
