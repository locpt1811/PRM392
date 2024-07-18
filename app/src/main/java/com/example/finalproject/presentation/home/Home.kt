package com.example.finalproject.presentation.home

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.finalproject.R
import com.example.finalproject.common.helper.PreferenceManager
import com.example.finalproject.model.shopping.BookDTO
import com.example.finalproject.presentation.home.address.AddressScreen
import com.example.finalproject.presentation.home.favorite.FavoritesScreen
import com.example.finalproject.presentation.home.product.ProductScreen
import com.example.finalproject.presentation.home.profile.ProfileScreen
import com.example.finalproject.presentation.home.search.SearchScreen
import com.example.finalproject.utils.REMEMBER_ME
import java.net.URLDecoder


fun NavGraphBuilder.addHomeGraph(
    onProductClick: (BookDTO, NavBackStackEntry) -> Unit,
    onSignOutClick: (NavBackStackEntry) -> Unit,
    onCartClick: (NavBackStackEntry) -> Unit,
    onChatListClick: (NavBackStackEntry) -> Unit,
    onNavigateToRoute: (String) -> Unit
) {
    composable(HomeSections.PRODUCT.route) { from ->
        ProductScreen(
            onProductClick = remember { { product -> onProductClick(product, from) } },
            onCartClick = remember { { onCartClick(from) } },
            onNavigateRoute = onNavigateToRoute,
            onChatListClick = remember { { onChatListClick(from) } },
            onNavigateToSearch = { query ->
                val searchRoute = "${HomeSections.SEARCH.route}?query=${query}"
                onNavigateToRoute(searchRoute)
            }
        )
    }

    composable(
        route = "${HomeSections.SEARCH.route}?query={query}",
        arguments = listOf(navArgument("query") { defaultValue = "" })
    ) { from ->
        val encodedQuery = from.arguments?.getString("query") ?: ""
        val decodedQuery = URLDecoder.decode(encodedQuery, "UTF-8")
        SearchScreen(
            initialQuery = decodedQuery,
            onProductClick = remember { {
                product -> onProductClick(product, from)
            } },
            onNavigateRoute = onNavigateToRoute,
        )
    }

//    composable(HomeSections.SEARCH.route) { from ->
//        SearchScreen(
//            onProductClick = remember { { product -> onProductClick(product, from) } },
//            onNavigateRoute = onNavigateToRoute
//        )
//    }

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
    )
}

@Composable
fun ShoppingAppTopBar(
    modifier: Modifier = Modifier,
    onNavigateToSearch: (String) -> Unit,
    onChatListClick: () -> Unit,
    onCartClick: () -> Unit
) {
    var searchActive by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (!searchActive) {
                    Modifier.padding(horizontal = dimensionResource(id = R.dimen.two_level_margin))
                } else {
                    Modifier.padding(horizontal = 0.dp)
                }
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SearchBarM3(
            modifier = Modifier.weight(1f),
            onNavigateToSearch = onNavigateToSearch,
            onActiveChange = { searchActive = it }
        )

        if (!searchActive) {
            Row {
                IconButton(onClick = onChatListClick) {
                    Icon(imageVector = Icons.Outlined.Email, contentDescription = null)
                }
                IconButton(onClick = onCartClick) {
                    Icon(imageVector = Icons.Outlined.ShoppingCart, contentDescription = null)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarM3(
    modifier: Modifier,
    onNavigateToSearch: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    val searchHistory = listOf("Android", "Kotlin", "Beatles", "Cliffs Notes", "Illuminati")

    SearchBar(
        modifier = modifier,
//            .padding(vertical = dimensionResource(id = R.dimen.one_level_margin)),
        query = query,
        onQueryChange = { query = it },
        onSearch = { newQuery ->
            onNavigateToSearch(newQuery)
            active = false
            onActiveChange(false)
        },
        active = active,
        onActiveChange = { isActive ->
            active = isActive
            onActiveChange(isActive)
        },
        placeholder = {
            Text(text = "Search")
        },
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
        },
        trailingIcon = {
            Row {
                IconButton(onClick = { /* open mic dialog */ }) {
                    Icon(imageVector = Icons.Filled.Mic, contentDescription = "Mic")
                }
                if (active) {
                    IconButton(
                        onClick = { if (query.isNotEmpty()) query = "" else active = false; onActiveChange(false) }
                    ) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                    }
                }
            }
        }
    ) {
        searchHistory.takeLast(3).forEach { item ->
            ListItem(
                modifier = Modifier.clickable { query = item; },
                headlineContent = { Text(text = item) },
                leadingContent = {
                    Icon(imageVector = Icons.Filled.History, contentDescription = "History")
                }
            )
        }
    }
}

@Composable
fun ShoppingAppBottomBar(
    tabs: Array<HomeSections>,
    currentRoute: String,
    navigateToRoute: (String) -> Unit,
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