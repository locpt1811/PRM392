package com.example.finalproject.model.shopping


import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import java.util.Date

@Immutable
@Serializable
data class UserProfileInfoDTO(
    val uuid: String,
    val first_name: String? = null,
    val last_name: String? = null
)

