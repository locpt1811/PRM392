package com.example.finalproject.model.shopping

import androidx.compose.runtime.Immutable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Immutable
@Serializable
data class OrderDTO(
    val id: Int,
    val created_at: String? = null,
    val address: String? = null,
    val user_id: String? = null,
    val status: String? = null,
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
data class OrderStatusDTO(
    val status_id: Int? = null,
    val status_value: String? = null
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

@Immutable
@Serializable
data class OrderEntity(
    val id: Int,
    val address: String? = null,
    @Serializable(with = DateSerializer::class) val created_at: Date? = null,
    val user_id: String? = null,
)

object DateSerializer : KSerializer<Date> {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(dateFormat.format(value))
    }

    override fun deserialize(decoder: Decoder): Date {
        return dateFormat.parse(decoder.decodeString()) ?: throw SerializationException("Invalid date format")
    }
}