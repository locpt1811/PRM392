package com.example.finalproject.data.mapper

import com.example.finalproject.model.shopping.BookDTO
import com.example.finalproject.model.shopping.BookEntity
import com.example.finalproject.model.shopping.Category
import com.example.finalproject.model.shopping.Publisher
import com.example.finalproject.model.shopping.BookLanguage
import com.example.finalproject.model.shopping.MessageDTO

fun BookDTO.toProductEntity(): BookEntity {
    return BookEntity(
        book_id = this.book_id,
        title = this.title,
        isbn13 = this.isbn13,
        num_pages = this.num_pages,
        image_url = this.image_url,
        description = this.description,
        rating = this.rating,
        price = this.price,
        user_id = this.user_id,

        language_id = this.book_language?.language_id,
        language_name = this.book_language?.language_name,
        language_code = this.book_language?.language_code,


        publisher_id = this.publisher?.publisher_id,
        publisher_name = this.publisher?.publisher_name,

        category_id = this.category?.category_id,
        category_name = this.category?.category_name,
    )
}

fun BookEntity.toBookDTO(): BookDTO {
    return BookDTO(
        book_id = this.book_id,
        title = this.title,
        isbn13 = this.isbn13,
        num_pages = this.num_pages,
        image_url = this.image_url,
        description = this.description,
        rating = this.rating,
        price = this.price,
        user_id = this.user_id,
        book_language = BookLanguage(
            language_id = this.language_id,
            language_name = this.language_name,
            language_code = this.language_code
        ),
        publisher = Publisher(
            publisher_id = this.publisher_id,
            publisher_name = this.publisher_name
        ),
        category = Category(
            category_id = this.category_id,
            category_name = this.category_name
        )
    )
}


// Extension function to map JSON to data classes
fun Map<String, Any>.toMessageDTO(): MessageDTO {
    return MessageDTO(
        id = this["id"] as String,
        chat_room_id = this["chat_room_id"] as String,
        user_id = this["user_id"] as String,
        content = this["content"] as String
    )
}