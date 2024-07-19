package com.example.finalproject.data.repository

import android.util.Log
import com.example.finalproject.R
import com.example.finalproject.common.Response
import com.example.finalproject.data.mapper.toBookDTO
import com.example.finalproject.data.mapper.toDto
import com.example.finalproject.domain.repository.OrderRepository
import com.example.finalproject.domain.repository.ProfileRepository
import com.example.finalproject.model.shopping.CartEntity
import com.example.finalproject.model.shopping.CreateOrderDTO
import com.example.finalproject.model.shopping.CreateOrderResponseDTO
import com.example.finalproject.model.shopping.OrderDTO
import com.example.finalproject.model.shopping.OrderEntity
import com.example.finalproject.model.shopping.UserProfileDTO
import com.example.finalproject.model.shopping.UserProfileInfo
import com.example.finalproject.model.shopping.UserProfileInfoDTO
import com.google.gson.Gson
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.RpcMethod
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.supabaseJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
class OrderRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest
) : OrderRepository {
    override suspend fun getOrders(): Response<List<OrderDTO>> {
        return withContext(Dispatchers.IO) {
            try {
                val result = postgrest
                    .rpc("get_orders_with_details",
                        RpcMethod.GET)
                    .decodeList<OrderDTO>()
                Log.e("OrderRepo", "Get get_orders_with_details: ${result}")

                Response.Success(result)
            } catch (e: Exception) {
                Log.e("OrderRepo", "Get exception: ${e.message}")
                Response.Error(errorMessageId = R.string.error_message_books)
            }
        }
    }

    override suspend fun getOrdersByUserId(userUuid: UUID): Response<List<OrderDTO>> {
        return withContext(Dispatchers.IO) {
            try {
                Log.e("OrderRepo", "Get getOrdersByUserId uuid: ${userUuid}")

                val result = postgrest
                    .rpc("get_orders_by_user_id?arg_user_id=${userUuid}",
                        RpcMethod.GET)
                    .decodeList<OrderDTO>()
                Log.e("OrderRepo", "Get getOrdersByUserId: ${result}")

                Response.Success(result)
            } catch (e: Exception) {
                Log.e("OrderRepo", "Get 1 exception: ${e.message}")
                Response.Error(errorMessageId = R.string.error_message_books)
            }
        }
    }
    override suspend fun getOrderByOrderId(orderId: Int): Response<OrderDTO> {
        return withContext(Dispatchers.IO) {
            try {
                val result = postgrest
                    .rpc("get_order_by_order_id?arg_order_id=${orderId}",
                        RpcMethod.GET)
                    .decodeSingle<OrderDTO>()

                Response.Success(result)
            } catch (e: Exception) {
                Log.e("OrderRepo", "Get 1 exception: ${e.message}")
                Response.Error(errorMessageId = R.string.error_message_books)
            }
        }
    }

    override suspend fun createOrder(orderDTO: CreateOrderDTO, items: List<CartEntity>): Response<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                // Insert order_user table
                val orderInsertResponse = postgrest.from("order_user")
                    .insert(mapOf(
                        "user_id" to orderDTO.user_id,
                        "address" to orderDTO.address
                    ))

                // Retrieve the new order ID using an RPC method
                val newOrder = postgrest
                    .rpc(
                        "get_new_order_by_user_id?arg_user_id=${orderDTO.user_id}",
                        RpcMethod.GET
                    ).decodeSingle<OrderEntity>()
                val orderId = newOrder.id

                // Prepare the items for insertion into order_item
                val orderItems = items.map { item ->
                    mapOf(
                        "book_id" to item.id,
                        "order_id" to orderId,
                        "quantity" to item.count
                    )
                }

                // Insert into order_item
                postgrest.from("order_item")
                    .insert(orderItems)

                Response.Success(Unit)
            } catch (e: Exception) {
                Log.e("OrderRepo", "Create order exception: ${e.message}")
                Response.Error(errorMessageId = R.string.error_message_books)
            }
        }
    }

}