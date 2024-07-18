package com.example.finalproject.presentation.myorder

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.common.Response
import com.example.finalproject.domain.repository.AuthRepository
import com.example.finalproject.domain.repository.OrderRepository
import com.example.finalproject.model.shopping.OrderDTO
import com.example.finalproject.presentation.navigation.MainDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyOrderDetailViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val savedStateHandle: SavedStateHandle,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _order = MutableStateFlow<OrderDTO?>(null)
    val order: StateFlow<OrderDTO?> = _order

    init {
        val orderId = savedStateHandle.get<Int>(MainDestinations.MY_ORDER_DETAIL_ID)
        orderId?.let { fetchOrderDetails(it) }
    }

    private fun fetchOrderDetails(orderId: Int) {
        viewModelScope.launch(ioDispatcher) {
            when (val response = orderRepository.getOrderByOrderId(orderId)) {
                is Response.Success -> {
                    _order.value = response.data
                }
                is Response.Error -> {
                    // Handle error case
                    Log.e("MyOrderDetailViewModel", "Error fetching order details: ${response}")
                }
            }
        }
    }
}