package com.example.finalproject.presentation.home.search

import android.util.Log
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finalproject.R
import com.example.finalproject.model.shopping.BookDTO
import com.example.finalproject.model.shopping.BookEntity
import com.example.finalproject.presentation.designsystem.components.ShoppingScaffold
import com.example.finalproject.presentation.designsystem.components.ShoppingShowToastMessage
import com.example.finalproject.presentation.designsystem.theme.ShoppingAppTheme
import com.example.finalproject.presentation.home.HomeSections
import com.example.finalproject.presentation.home.ShoppingAppBottomBar
import com.example.finalproject.presentation.home.product.ShoppingProductItem
import com.example.finalproject.utils.CustomPreview

@Composable
fun SearchScreen(
    initialQuery : String,
    modifier: Modifier = Modifier,
    onProductClick: (BookDTO) -> Unit,
    onNavigateRoute: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    LaunchedEffect(initialQuery) {
        viewModel.setQueryToEmpty()
        viewModel.searchedText = initialQuery
        viewModel.searchTitle(initialQuery)
    }

    DisposableEffect(initialQuery) {
        onDispose {
            viewModel.setQueryToEmpty()
        }
    }

    val uiState by viewModel.uiState.collectAsState()

    if (uiState.errorMessages.isNotEmpty()) {
        ShoppingShowToastMessage(message = uiState.errorMessages.first().asString())
        viewModel.errorConsumed()
    }

    ShoppingScaffold(
        modifier = modifier,
        bottomBar = {
            ShoppingAppBottomBar(
                tabs = HomeSections.values(),
                currentRoute = HomeSections.SEARCH.route,
                navigateToRoute = onNavigateRoute
            )
        }
    ) { paddingValues ->
        SearchScreenContent(
            modifier = Modifier.padding(paddingValues),
            searchValue = viewModel.searchedText,
            onSearchValChanged = viewModel::onSearchValueChange,
            searchResult = uiState.searchResult,
            isSearchResultEmpty = uiState.isSearchResultEmpty,
            onProductClick = onProductClick
        )
    }
}

@Composable
private fun SearchScreenContent(
    modifier: Modifier,
    searchValue: String,
    onSearchValChanged: (String) -> Unit,
    searchResult: List<BookEntity>,
    isSearchResultEmpty: Boolean,
    onProductClick: (BookDTO) -> Unit
) {
    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.two_level_margin)),
                value = searchValue,
                onValueChange = onSearchValChanged,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                placeholder = {
                    Text(text = stringResource(id = R.string.search))
                }
            )
            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(
                    vertical = dimensionResource(id = R.dimen.two_level_margin)
                ),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.two_level_margin)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.two_level_margin))
            ) {
                items(searchResult, key = { it.book_id }) {
                    ShoppingProductItem(
                        book_id = it.book_id,
                        title = it.title,
                        isbn13 = it.isbn13,
                        num_pages = it.num_pages,
                        image_url = it.image_url,
                        description = it.description,
                        rating = it.rating,
                        price = it.price,
                        user_id = it.user_id,

                        language_id = it.language_id,
                        language_code = it.language_code,
                        language_name = it.language_name,

                        publisher_id = it.publisher_id,
                        publisher_name = it.publisher_name,

                        category_id = it.category_id,
                        category_name = it.category_name,

                        onProductClick = onProductClick
                    )
                }
            }

            if (isSearchResultEmpty) {
                SearchResultEmptyView(
                    R.drawable.search_result_empty,
                    R.string.search_result_empty_message
                )
            } else if (searchResult.isEmpty()) {
                SearchResultEmptyView(
                    R.drawable.search,
                    R.string.search_something
                )
            }
        }
    }
}

@Composable
private fun SearchResultEmptyView(
    imageId: Int,
    messageId: Int,
    imageSize: Dp = 112.dp
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(imageSize),
            painter = painterResource(id = imageId),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.two_level_margin))
                .padding(horizontal = dimensionResource(id = R.dimen.four_level_margin)),
            text = stringResource(id = messageId),
            textAlign = TextAlign.Center
        )
    }
}

@CustomPreview
@Composable
private fun SearchScreenNoSearchPreview() {
    ShoppingAppTheme {
        Surface {
            SearchScreenContent(
                modifier = Modifier,
                searchValue = "",
                onSearchValChanged = {},
                searchResult = listOf(),
                isSearchResultEmpty = false,
                onProductClick = {}
            )
        }
    }
}

@CustomPreview
@Composable
private fun SearchScreenPreview() {
    ShoppingAppTheme {
        Surface {
            SearchScreenContent(
                modifier = Modifier,
                searchValue = "",
                onSearchValChanged = {},
                searchResult = List(3) {
                    BookEntity(
                        it,
                        "Preview Title",
                        (10 * it).toString(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        category_name = null
                    )
                },
                isSearchResultEmpty = false,
                onProductClick = {}
            )
        }
    }
}

@CustomPreview
@Composable
private fun SearchScreenEmptyPreview() {
    ShoppingAppTheme {
        Surface {
            SearchScreenContent(
                modifier = Modifier,
                searchValue = "",
                onSearchValChanged = {},
                searchResult = listOf(),
                isSearchResultEmpty = true,
                onProductClick = {}
            )
        }
    }
}