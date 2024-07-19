package com.example.finalproject.model.shopping
import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
@Immutable
@Serializable
@Entity
data class UserProfileInfo(
    @PrimaryKey
    @ColumnInfo("id")
    val id: String,

    @ColumnInfo("first_name")
    val firstName: String?,

    @ColumnInfo("last_name")
    val lastName: String?,

    @ColumnInfo("avatar_url")
    val avatarUrl: String?


)