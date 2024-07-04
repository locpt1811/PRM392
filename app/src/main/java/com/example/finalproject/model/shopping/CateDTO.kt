package com.example.finalproject.model.shopping

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class CateDTO (
    val category_id: Int,
    val category_name: String? = null
)

@Immutable
@Entity
data class CateEntity(
    @PrimaryKey
    @ColumnInfo("category_id")
    val category_id: Int,

    @ColumnInfo("category_name")
    val category_name: String?,
)