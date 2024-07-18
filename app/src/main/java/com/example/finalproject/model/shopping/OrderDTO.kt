package com.example.finalproject.model.shopping

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import java.util.Date

@Immutable
@Serializable
data class OrderDTO(
    val id: Int,
    val created_at: String? = null,
    val address: String? = null,
    val user_id: String? = null,
    val items: List<OrderItemDTO>
)

@Immutable
@Serializable
data class OrderItemDTO(
    val id: Int,
    val order_id: Int,
    val book_id: Int,
    val quantity: Int,
    val book: BookDTO
)

@Immutable
@Serializable
data class CreateOrderDTO(
    val address: String? = null,
    val user_id: String? = null
)
@Immutable
@Serializable
data class CreateOrderResponseDTO(
    val id: String? = null
)