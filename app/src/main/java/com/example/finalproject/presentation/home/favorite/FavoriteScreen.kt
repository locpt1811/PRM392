package com.example.finalproject.presentation.home.favorite

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finalproject.R
import com.example.finalproject.data.mapper.toProduct
import com.example.finalproject.model.shopping.BookDTO
import com.example.finalproject.model.shopping.BookEntity
import com.example.finalproject.presentation.designsystem.components.FullScreenCircularLoading
import com.example.finalproject.presentation.designsystem.components.ShoppingScaffold
import com.example.finalproject.presentation.designsystem.theme.ShoppingAppTheme
import com.example.finalproject.presentation.home.HomeSections
import com.example.finalproject.presentation.home.ShoppingAppBottomBar
import com.example.finalproject.utils.CustomPreview

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    onProductClick: (BookDTO) -> Unit,
    onNavigateRoute: (String) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    viewModel.getAllFavoriteProducts()

    if (uiState.userMessages.isNotEmpty()) {
        val context = LocalContext.current
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(message = uiState.userMessages.first().asString(context))
            viewModel.userMessagesConsumed()
        }
    }

    ShoppingScaffold(
        modifier = modifier,
        bottomBar = {
            ShoppingAppBottomBar(
                tabs = HomeSections.values(),
                currentRoute = HomeSections.FAVORITES.route,
                navigateToRoute = onNavigateRoute
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        FavoritesScreenContent(
            modifier = Modifier.padding(paddingValues),
            isLoading = uiState.isLoading,
            favoriteProductList = uiState.favoriteList,
            onRemoveFavoriteClicked = viewModel::removeProductFromFavorites,
            onFavoriteItemClicked = onProductClick
        )
    }
}

@Composable
private fun FavoritesScreenContent(
    modifier: Modifier,
    isLoading: Boolean,
    favoriteProductList: List<BookEntity>,
    onRemoveFavoriteClicked: (Int?) -> Unit,
    onFavoriteItemClicked: (BookDTO) -> Unit
) {
    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                FullScreenCircularLoading()
            }

            if (favoriteProductList.isNotEmpty()) {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.two_level_margin)),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.two_level_margin)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.two_level_margin))
                ) {
                    items(favoriteProductList, key = { it.book_id }) {
                        FavoriteItem(
                            imgUrl = it.image_url ?: "",
                            title = it.title ?: "",
                            price = it.price ?: 0.0,
                            onRemoveFavoriteClicked = remember {
                                { onRemoveFavoriteClicked(it.book_id) }
                            },
                            onFavoriteItemClicked = remember {
                                { onFavoriteItemClicked(it.toProduct()) }
                            }
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        modifier = Modifier.size(112.dp),
                        painter = painterResource(id = R.drawable.search_result_empty),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = dimensionResource(id = R.dimen.two_level_margin))
                            .padding(horizontal = dimensionResource(id = R.dimen.four_level_margin)),
                        text = stringResource(id = R.string.no_favorite),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@CustomPreview
@Composable
private fun FavoritesScreenPreview() {
    ShoppingAppTheme {
        Surface {
            FavoritesScreenContent(
                modifier = Modifier,
                isLoading = false,
                favoriteProductList = listOf(
                    BookEntity(
                        0,
                        "This is a preview title",
                        "10",
                        num_pages = 500,
                        image_url = "https://example.com/product1.jpg",
                        description = "This is a preview description",
                        rating = 5.0,
                        price = 10.0,
                        language_id = 1,
                        language_code = "en-us",
                        language_name = "US English",
                        publisher_id = 1,
                        publisher_name = "preview publisher",
                        category_id = 1,
                        category_name = "preview category",
                    )
                ),
                onRemoveFavoriteClicked = {},
                onFavoriteItemClicked = {}
            )
        }
    }
}

@CustomPreview
@Composable
private fun FavoritesScreenEmptyFavoriteListPreview() {
    ShoppingAppTheme {
        Surface {
            FavoritesScreenContent(
                modifier = Modifier,
                isLoading = false,
                favoriteProductList = listOf(),
                onRemoveFavoriteClicked = {},
                onFavoriteItemClicked = {}
            )
        }
    }
}