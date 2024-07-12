package com.example.finalproject.presentation.home

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.finalproject.R
import com.example.finalproject.common.helper.PreferenceManager
import com.example.finalproject.domain.repository.AuthRepository
import com.example.finalproject.model.shopping.BookDTO
import com.example.finalproject.presentation.chat.ChatScreen
import com.example.finalproject.presentation.home.address.AddressScreen
import com.example.finalproject.presentation.home.favorite.FavoritesScreen
import com.example.finalproject.presentation.home.product.ProductScreen
import com.example.finalproject.presentation.home.profile.ProfileScreen
import com.example.finalproject.presentation.home.search.SearchScreen
import com.example.finalproject.utils.REMEMBER_ME

fun NavGraphBuilder.addHomeGraph(
    onProductClick: (BookDTO, NavBackStackEntry) -> Unit,
    onSignOutClick: (NavBackStackEntry) -> Unit,
    onCartClick: (NavBackStackEntry) -> Unit,
    onNavigateToRoute: (String) -> Unit
) {
    composable(HomeSections.PRODUCT.route) { from ->
        ProductScreen(
            onProductClick = remember { { product -> onProductClick(product, from) } },
            onCartClick = remember { { onCartClick(from) } },
            onNavigateRoute = onNavigateToRoute
        )
    }
    composable(HomeSections.SEARCH.route) { from ->
        SearchScreen(
            onProductClick = remember { { product -> onProductClick(product, from) } },
            onNavigateRoute = onNavigateToRoute
        )
    }
    composable(HomeSections.FAVORITES.route) { from ->
        FavoritesScreen(
            onProductClick = remember {
                { product -> onProductClick(product, from) }
            },
            onNavigateRoute = onNavigateToRoute
        )
    }
    composable(HomeSections.PROFILE.route) { from ->
        val preferenceManager = PreferenceManager(LocalContext.current)
        ProfileScreen(
            onSignOutClicked = remember {
                {
//                    FirebaseAuth.getInstance().signOut()
                    preferenceManager.saveData(REMEMBER_ME, false)
                    onSignOutClick(from)

                }
            },
            onNavigateRoute = onNavigateToRoute
        )
    }

    composable(HomeSections.ADDRESS.route) { from ->
        AddressScreen(
            onNavigateRoute = onNavigateToRoute
        )
    }
    composable(HomeSections.CHAT.route + "/{chatRoomId}") { backStackEntry ->
        val chatRoomId = backStackEntry.arguments?.getString("chatRoomId")
        chatRoomId?.let {
            ChatScreen(chatRoomId = it)
        }
    }
}




enum class HomeSections(
    @StringRes val title: Int,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val route: String
) {
    PRODUCT(R.string.home,
        Icons.Filled.Home,
        Icons.Outlined.Home,
        "home/product"
    ),
    SEARCH(R.string.search,
        Icons.Filled.Search,
        Icons.Outlined.Search,
        "home/search"
    ),
    FAVORITES(
        R.string.favorites,
        Icons.Filled.Favorite,
        Icons.Outlined.FavoriteBorder,
        "home/favorite"
    ),
    PROFILE(
        R.string.profile,
        Icons.Filled.AccountCircle,
        Icons.Outlined.AccountCircle,
        "home/profile"
    ),
    ADDRESS(
        R.string.address,
        Icons.Filled.LocationOn,
        Icons.Outlined.LocationOn,
        "home/address"
    ),
    CHAT(
        R.string.chat,
        Icons.Filled.Home,
        Icons.Outlined.Home,
        "home/chat" // Define your route here
    )
}

@Composable
fun ShoppingAppBottomBar(
    tabs: Array<HomeSections>,
    currentRoute: String,
    navigateToRoute: (String) -> Unit
) {
    val currentSection = tabs.first { it.route == currentRoute }

    NavigationBar {
        tabs.forEach { section ->
            NavigationBarItem(
                selected = currentSection.route == section.route,
                onClick = {
                    if (currentSection.route != section.route) {
                        navigateToRoute(section.route)
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (currentSection.route == section.route) section.selectedIcon else section.unSelectedIcon,
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = stringResource(id = section.title))
                }
            )
        }
    }
}