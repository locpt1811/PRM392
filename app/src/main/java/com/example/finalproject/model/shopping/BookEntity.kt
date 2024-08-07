package com.example.finalproject.model.shopping

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.Date

@Immutable
@Serializable
@Entity
data class BookEntity(
    @PrimaryKey
    @ColumnInfo("book_id")
    val book_id: Int,

    @ColumnInfo("title")
    val title: String?,

    @ColumnInfo("isbn13")
    val isbn13: String?,

    @ColumnInfo("num_pages")
    val num_pages: Int?,

    @ColumnInfo("image_url")
    val image_url: String?,

    @ColumnInfo("description")
    val description: String?,

    @ColumnInfo("rating")
    val rating: Double?,

    @ColumnInfo("price")
    val price: Double?,


    // Language
    @ColumnInfo("language_id")
    val language_id: Int?,

    @ColumnInfo(name = "language_code")
    val language_code: String?,

    @ColumnInfo("language_name")
    val language_name: String?,



    // Publisher
    @ColumnInfo("publisher_id")
    val publisher_id: Int?,

    @ColumnInfo("publisher_name")
    val publisher_name: String?,

    @ColumnInfo("user_id")
    val user_id: String?,

    // Category
    @ColumnInfo("category_id")
    val category_id: Int?,

    @ColumnInfo("category_name")
    val category_name: String?
)