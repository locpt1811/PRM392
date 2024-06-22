package com.example.finalproject

import androidx.navigation.NavType
import androidx.navigation.navArgument


interface Destination {
    val route: String
    val title: String
}


object BookListDestination : Destination {
    override val route = "book_list"
    override val title = "Book List"
}

object BookDetailsDestination : Destination {
    override val route = "book_details"
    override val title = "Book Details"
    const val bookId = "book_id"
    val arguments = listOf(navArgument(name = bookId) {
        type = NavType.StringType
    })
    fun createRouteWithParam(productId: String) = "$route/${bookId}"
}

//TODO
object AddProductDestination : Destination {
    override val route = "add_product"
    override val title = "Add Product"
}

object AuthenticationDestination: Destination {
    override val route = "authentication"
    override val title = "Authentication"
}

object SignUpDestination: Destination {
    override val route = "signup"
    override val title = "Sign Up"
}
