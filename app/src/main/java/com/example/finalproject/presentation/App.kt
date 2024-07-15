package com.example.finalproject.presentation

import android.content.IntentSender.OnFinished
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.finalproject.model.shopping.BookDTO
import com.example.finalproject.presentation.cart.CartScreen
import com.example.finalproject.presentation.chat.ChatListScreen
import com.example.finalproject.presentation.chat.ChatScreen
import com.example.finalproject.presentation.checkout.CheckoutScreen
import com.example.finalproject.presentation.designsystem.theme.ShoppingAppTheme
import com.example.finalproject.presentation.home.HomeSections
import com.example.finalproject.presentation.home.addHomeGraph
import com.example.finalproject.presentation.login.LoginScreen
import com.example.finalproject.presentation.navigation.MainDestinations
import com.example.finalproject.presentation.navigation.rememberShoppingAppNavController
import com.example.finalproject.presentation.onboarding.OnboardingScreen
import com.example.finalproject.presentation.payment.PaymentScreen
import com.example.finalproject.presentation.product_detail.ProductDetailScreen
import com.example.finalproject.presentation.signup.SignUpScreen


@Composable
fun ShoppingApp(startDestination: String) {
    ShoppingAppTheme {
        val shoppingAppNavController = rememberShoppingAppNavController()
        NavHost(
            navController = shoppingAppNavController.navController,
            startDestination = startDestination,
        ) {
            shoppingAppGraph(
                navController = shoppingAppNavController.navController,
                onProductClick = shoppingAppNavController::navigateToProduct,
                onSignOutClick = shoppingAppNavController::onNavigateLogin,
                onCartClick = shoppingAppNavController::navigateCart,
                onSignUpClick = shoppingAppNavController::navigateToSignUp,
                onLoginClick = shoppingAppNavController::navigateHome,
                onPaymentClick = shoppingAppNavController::navigatePayment,
                onChatClick = shoppingAppNavController::navigateToChat,
                onChatListClick = shoppingAppNavController::navigateToChatList,
                onGooglePayButtonClick = shoppingAppNavController::navigateGGPayment,
                onContinueShoppingClick = shoppingAppNavController::navigateHome,
                onFinished = shoppingAppNavController::navigateHome,
                upPress = shoppingAppNavController::upPress,
                onNavigateToRoute = shoppingAppNavController::navigateToBottomBarRoute,
            )
        }
    }
}

private fun NavGraphBuilder.shoppingAppGraph(
    navController: NavController,
    onProductClick: (BookDTO, NavBackStackEntry) -> Unit,
    onSignOutClick: (NavBackStackEntry) -> Unit,
    onCartClick: (NavBackStackEntry) -> Unit,
    onSignUpClick: (NavBackStackEntry) -> Unit,
    onLoginClick: (NavBackStackEntry) -> Unit,
    onPaymentClick: (Float, NavBackStackEntry) -> Unit,
    onGooglePayButtonClick: (Float, NavBackStackEntry) -> Unit,
    onContinueShoppingClick: (NavBackStackEntry) -> Unit,
    onFinished: (NavBackStackEntry) -> Unit,
    upPress: () -> Unit,
    onChatClick: (String, String, NavBackStackEntry) -> Unit,
    onChatListClick: (NavBackStackEntry) -> Unit,
    onNavigateToRoute: (String) -> Unit
) {
    navigation(
        route = MainDestinations.PRODUCT_ROUTE,
        startDestination = HomeSections.PRODUCT.route
    ) {
        addHomeGraph(onProductClick, onSignOutClick, onCartClick, onChatListClick, onNavigateToRoute)
    }

    composable(route = MainDestinations.LOGIN_ROUTE) { from ->
        LoginScreen(
            navController = navController,
            onSignUpClick = remember { { onSignUpClick(from) } },
            onLoginClick = remember { { onLoginClick(from) } }
        )
    }
    composable(route = MainDestinations.SIGNUP_ROUTE) {
        SignUpScreen(navController = navController,upPress = upPress)
    }

    composable(route = MainDestinations.ONBOARDING_ROUTE) { from ->
        OnboardingScreen(onFinished = remember { { onFinished(from) } })
    }

    composable(route = MainDestinations.CART_ROUTE) { from ->
        CartScreen(
            onGooglePayButtonClick = remember { { amount -> onGooglePayButtonClick(amount, from) } },
            onPaymentClick = remember { { amount -> onPaymentClick(amount, from) } })
    }

    composable(
        route = "${MainDestinations.PAYMENT_ROUTE}/{${MainDestinations.PAYMENT_AMOUNT_KEY}}",
        arguments = listOf(navArgument(MainDestinations.PAYMENT_AMOUNT_KEY) {
            type = NavType.FloatType
        })
    ) { from ->
        PaymentScreen(onContinueShoppingClick = remember { { onContinueShoppingClick(from) } })
    }

    composable(
        route = "${MainDestinations.GG_PAYMENT_ROUTE}/{${MainDestinations.PAYMENT_AMOUNT_KEY}}",
        arguments = listOf(navArgument(MainDestinations.PAYMENT_AMOUNT_KEY) {
            type = NavType.FloatType
        })
    ) { from ->
        CheckoutScreen(onContinueShoppingClick = remember { { onContinueShoppingClick(from) } })
    }

    composable(
        route = "${MainDestinations.PRODUCT_DETAIL_ROUTE}/{${MainDestinations.PRODUCT_DETAIL_KEY}}",
        arguments = listOf(
            navArgument(MainDestinations.PRODUCT_DETAIL_KEY) { type = NavType.StringType }
        )
    ) { from ->
        ProductDetailScreen(
            onBackClick = { navController.popBackStack() },
            onCartClick = remember { { onCartClick(from) } },
            onChatClick = { userId, otherUserId ->
                //navController.navigate("${MainDestinations.CHAT_ROUTE}/$userId/$otherUserId")
                onChatClick(userId,otherUserId,from)
            }
        )
    }


    composable(
        route = "${MainDestinations.CHAT_ROUTE}/{${MainDestinations.CHAT_USER_ID}}/{${MainDestinations.CHAT_OTHER_USER_ID}}",
        arguments = listOf(
            navArgument(MainDestinations.CHAT_USER_ID) { type = NavType.StringType },
            navArgument(MainDestinations.CHAT_OTHER_USER_ID) { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val userId = backStackEntry.arguments?.getString("userId") ?: ""
        val otherUserId = backStackEntry.arguments?.getString("otherUserId") ?: ""
        ChatScreen(
            userId = userId,
            otherUserId = otherUserId,
            onBackClick = { navController.popBackStack() },
        )
    }

    composable(route = MainDestinations.CHAT_LIST_ROUTE) { from ->
        ChatListScreen(
            onChatIconClick = { userId, otherUserId ->
                onChatClick(userId,otherUserId,from)
            }
        )
    }

}