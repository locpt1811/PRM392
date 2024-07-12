package com.example.finalproject.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.finalproject.model.shopping.BookDTO
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

object MainDestinations {
    const val ONBOARDING_ROUTE = "onboarding"
    const val LOGIN_ROUTE = "login"
    const val SIGNUP_ROUTE = "signup"
    const val PRODUCT_ROUTE = "product"
    const val CART_ROUTE = "cart"
    const val PRODUCT_DETAIL_ROUTE = "productDetail"
    const val PRODUCT_DETAIL_KEY = "productObject"
    const val PAYMENT_ROUTE = "payment"
    const val GG_PAYMENT_ROUTE = "ggpayment"
    const val PAYMENT_AMOUNT_KEY = "paymentAmount"
    const val ADDRESS_ROUTE = "address"
    const val CHAT_ROUTE = "chat"
}

@Composable
fun rememberShoppingAppNavController(
    navController: NavHostController = rememberNavController()
): ShoppingAppNavController = remember(navController) {
    ShoppingAppNavController(navController)
}

@Stable
class ShoppingAppNavController @Inject constructor(
    val navController: NavHostController
) {

    private val currentRoute: String?
        get() = navController.currentDestination?.route

    fun upPress() {
        navController.navigateUp()
    }

    fun navigateToBottomBarRoute(route: String) {
        if (route != currentRoute) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                popUpTo(findStartDestination(navController.graph).id) {
                    saveState = true
                }
            }
        }
    }

    fun navigateToProduct(product: BookDTO, from: NavBackStackEntry) {
        if (shouldNavigate(from)) {
            val encodedValue =
                URLEncoder.encode(
                    Gson().toJson(product),
                    StandardCharsets.UTF_8.toString()
                )
            navController.navigate("${MainDestinations.PRODUCT_DETAIL_ROUTE}/$encodedValue")
        }
    }

    fun navigateToSignUp(from: NavBackStackEntry) {
        if (shouldNavigate(from)) {
            navController.navigate(MainDestinations.SIGNUP_ROUTE)
        }
    }

    fun navigateToOnboarding(from: NavBackStackEntry) {
        if (shouldNavigate(from)) {
            navController.navigate(MainDestinations.ONBOARDING_ROUTE)
        }
    }

    fun navigateToChat(chatRoomId: String) {
        navController.navigate("${MainDestinations.CHAT_ROUTE}/$chatRoomId") {
            launchSingleTop = true
            restoreState = true
            popUpTo(findStartDestination(navController.graph).id) {
                saveState = true
            }
        }
    }
    fun onNavigateLogin(from: NavBackStackEntry) {
        if (shouldNavigate(from)) {
            navController.navigate(MainDestinations.LOGIN_ROUTE) {
                popUpTo(0)
            }
        }
    }

    fun navigatePayment(totalAmount: Float, from: NavBackStackEntry) {
        if (shouldNavigate(from)) {
            navController.navigate("${MainDestinations.PAYMENT_ROUTE}/$totalAmount")
        }
    }

    fun navigateGGPayment(totalAmount: Float, from: NavBackStackEntry) {
        if (shouldNavigate(from)) {
            navController.navigate("${MainDestinations.GG_PAYMENT_ROUTE}/$totalAmount")
        }
    }

    fun navigateCart(from: NavBackStackEntry) {
        if (shouldNavigate(from)) {
            navController.navigate(MainDestinations.CART_ROUTE)
        }
    }

    fun navigateHome(from: NavBackStackEntry) {
        if (shouldNavigate(from)) {
            navController.navigate(MainDestinations.PRODUCT_ROUTE) {
                popUpTo(MainDestinations.PRODUCT_ROUTE) {
                    inclusive = true
                }
            }
        }
    }

    fun navigateToMap(from: NavBackStackEntry) {
        if (shouldNavigate(from)) {
            navController.navigate(MainDestinations.ADDRESS_ROUTE)
        }
    }


}

private fun shouldNavigate(from: NavBackStackEntry): Boolean = from.lifecycleIsResumed()

private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}