package com.example.finalproject.domain.repository

import com.example.finalproject.common.Response
import com.example.finalproject.model.shopping.BookDTO
import com.example.finalproject.model.shopping.CartEntity
import com.example.finalproject.model.shopping.CreateOrderDTO
import com.example.finalproject.model.shopping.OrderDTO
import com.example.finalproject.model.shopping.OrderStatusDTO
import com.example.finalproject.model.shopping.UserProfileDTO
import com.example.finalproject.model.shopping.UserProfileInfoDTO
import java.util.UUID

interface OrderRepository {
    suspend fun getOrderStatuses(): Response<List<OrderStatusDTO>>
    suspend fun getOrderByOrderId(orderId: Int): Response<OrderDTO>
    suspend fun getOrdersByUserId(userUuid: UUID): Response<List<OrderDTO>>
    suspend fun getOrders(): Response<List<OrderDTO>>
    suspend fun createOrder(orderDTO: CreateOrderDTO, items: List<CartEntity>): Response<Unit>
    suspend fun updateOrderStatus(orderId: Int, statusId: Int): Response<Boolean>
}
