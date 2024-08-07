package com.example.finalproject.presentation.navigation

import android.util.Log
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
import com.example.finalproject.presentation.home.HomeSections
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
    const val MY_ORDERS_ROUTE = "myOrders"
    const val MY_ORDER_DETAIL_ROUTE = "myOrderDetail"
    const val MY_ORDER_DETAIL_ID = "myOrderDetailId"
    const val MANAGE_ORDER_ROUTE = "manageOrder"
    const val CHAT_ROUTE = "chat"
    const val CHAT_USER_ID = "userId"
    const val CHAT_OTHER_USER_ID = "otherUserId"
    const val CHAT_LIST_ROUTE = "chatList"
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
                restoreState = false
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

//    fun navigateToSearch(query: String, from: NavBackStackEntry) {
//        if (shouldNavigate(from)) {
//            val encodedValue =
//                URLEncoder.encode(
//                    query,"UTF-8"
//                )
//            navController.navigate("${HomeSections.SEARCH.route}?query=${encodedValue}")
//        }
//    }

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

    fun navigateGGPayment(from: NavBackStackEntry) {
        if (shouldNavigate(from)) {
            navController.navigate(MainDestinations.GG_PAYMENT_ROUTE)
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

    fun navigateToChat(userId: String, otherUserId: String, from: NavBackStackEntry) {
        if (shouldNavigate(from)) {
            navController.navigate("${MainDestinations.CHAT_ROUTE}/$userId/$otherUserId")
        }
    }

    fun navigateToChatList(from: NavBackStackEntry) {
        if (shouldNavigate(from)) {
            navController.navigate(MainDestinations.CHAT_LIST_ROUTE)
        }
    }
    fun navigateToMyOrders(from: NavBackStackEntry) {
        if (shouldNavigate(from)) {
            navController.navigate(MainDestinations.MY_ORDERS_ROUTE)
        }
    }
    fun navigateToManageOrder(from: NavBackStackEntry) {
        if (shouldNavigate(from)) {
            navController.navigate(MainDestinations.MANAGE_ORDER_ROUTE)
        }
    }
    fun navigateToMyOrderDetail(orderId: Int, from: NavBackStackEntry) {
        if (shouldNavigate(from)) {
            navController.navigate("${MainDestinations.MY_ORDER_DETAIL_ROUTE}/$orderId")
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