package com.example.finalproject.presentation.home.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finalproject.R
import com.example.finalproject.common.helper.UiText
import com.example.finalproject.model.shopping.BookDTO
import com.example.finalproject.presentation.designsystem.components.ShoppingProductItem
import com.example.finalproject.presentation.designsystem.components.ShoppingScaffold
import com.example.finalproject.presentation.designsystem.theme.ShoppingAppTheme
import com.example.finalproject.presentation.home.HomeSections
import com.example.finalproject.presentation.home.ShoppingAppBottomBar
import com.example.finalproject.utils.CustomPreview
import java.util.Date

@Composable
fun ProductScreen(
    modifier: Modifier = Modifier,
    onProductClick: (BookDTO) -> Unit,
    onCartClick: () -> Unit,
    onNavigateRoute: (String) -> Unit,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    ShoppingScaffold(
        modifier = modifier,
        bottomBar = {
            ShoppingAppBottomBar(
                tabs = HomeSections.values(),
                currentRoute = HomeSections.PRODUCT.route,
                navigateToRoute = onNavigateRoute
            )
        }
    ) { paddingValues ->
        ProductScreenContent(
            modifier = Modifier.padding(paddingValues),
            categories = uiState.categoryUiState.categoryList,
            isCategoriesLoading = uiState.categoryUiState.isLoading,
            productList = uiState.productUiState.productList,
            isProductListLoading = uiState.productUiState.isLoading,
            onProductClick = onProductClick,
            onShoppingCartClicked = onCartClick,
            errors = uiState.errorMessages
        )
    }
}

@Composable
private fun ProductScreenContent(
    modifier: Modifier,
    categories: List<String>,
    isCategoriesLoading: Boolean,
    productList: List<BookDTO>,
    isProductListLoading: Boolean,
    onProductClick: (BookDTO) -> Unit,
    onShoppingCartClicked: () -> Unit,
    errors: List<UiText>
) {
    val allKeyword = stringResource(id = R.string.all)
    var selectedCatName by rememberSaveable { mutableStateOf(allKeyword) }

    Surface(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (errors.isNotEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = dimensionResource(id = R.dimen.four_level_margin)),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            modifier = Modifier.fillMaxWidth(),
                            painter = painterResource(id = R.drawable.error_img),
                            contentDescription = null,
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = dimensionResource(id = R.dimen.one_level_margin)),
                            text = errors.first().asString(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimensionResource(id = R.dimen.two_level_margin),
                            top = dimensionResource(id = R.dimen.one_level_margin)
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.discover_products),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    IconButton(onClick = onShoppingCartClicked) {
                        Icon(imageVector = Icons.Outlined.ShoppingCart, contentDescription = null)
                    }
                }
                CategoryList(
                    categories = categories,
                    isCategoriesLoading = isCategoriesLoading,
                    selectedCatName = selectedCatName,
                    onCategoryClick = remember { { selectedCatName = it } }
                )
                ProductList(
                    productList = productList,
                    isProductListLoading = isProductListLoading,
                    selectedCatName = selectedCatName,
                    onProductClick = onProductClick,
                    allCatText = stringResource(id = R.string.all)
                )
            }
        }
    }
}

@Composable
private fun CategoryList(
    categories: List<String>,
    isCategoriesLoading: Boolean,
    selectedCatName: String,
    onCategoryClick: (String) -> Unit
) {
    if (isCategoriesLoading) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.two_level_margin)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.two_level_margin)),
            contentPadding = PaddingValues(horizontal = dimensionResource(id = R.dimen.two_level_margin)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin))
        ) {
            item {
                CategoryItem(
                    categoryName = stringResource(id = R.string.all),
                    selectedCatName = selectedCatName,
                    onCategoryClick = onCategoryClick
                )
            }
            items(categories, key = { it }) { category ->
                CategoryItem(
                    categoryName = category,
                    selectedCatName = selectedCatName,
                    onCategoryClick = onCategoryClick
                )
            }
        }
    }
}

@Composable
private fun ProductList(
    productList: List<BookDTO>,
    isProductListLoading: Boolean,
    selectedCatName: String,
    onProductClick: (BookDTO) -> Unit,
    allCatText: String
) {
    if (isProductListLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.one_level_margin)),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(dimensionResource(id = R.dimen.one_level_margin)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin))
        ) {
            items(
                if (selectedCatName == allCatText) productList
                else productList.filter { it.category?.category_name?.uppercase() == selectedCatName.uppercase() },
                key = { it.book_id }
            ) {
                ShoppingProductItem(
                    book_id = it.book_id,
                    title = it.title,
                    isbn13 = it.isbn13,
                    num_pages = it.num_pages,
                    publication_date = it.publication_date,
                    image_url = it.image_url,
                    description = it.description,
                    rating = it.rating,
                    price = it.price,

                    language_id = it.book_language?.language_id,
                    language_code = it.book_language?.language_code,
                    language_name = it.book_language?.language_name,

                    publisher_id = it.publisher?.publisher_id,
                    publisher_name = it.publisher?.publisher_name,

                    category_id = it.category?.category_id,
                    category_name = it.category?.category_name,

                    onProductClick = onProductClick
                )
            }
        }
    }
}

@CustomPreview
@Composable
private fun ProductScreenPreview() {
    ShoppingAppTheme {
        Surface {
            ProductScreenContent(
                modifier = Modifier,
                categories = listOf(
                    "technology",
                    "electronics",
                    "jewelery",
                    "men's clothing",
                    "women's clothing"
                ),
                isCategoriesLoading = false,
                productList = List(3) {
                    BookDTO(
                        book_id = it,
                        title = "Product $it",
                        isbn13 = "1234567890123",
                        num_pages = 100,
                        publication_date = Date.from(Date().toInstant().plusSeconds(it.toLong())),
                        image_url = "https://example.com/product$it.jpg",
                        description = "This is a product description",
                        rating = 4.5,
                        price = 19.99
                    )
                },
                isProductListLoading = false,
                onProductClick = {},
                onShoppingCartClicked = {},
                errors = listOf()
            )
        }
    }
}

@CustomPreview
@Composable
private fun ProductScreenErrorPreview() {
    ShoppingAppTheme {
        Surface {
            ProductScreenContent(
                modifier = Modifier,
                categories = listOf(),
                isCategoriesLoading = false,
                productList = listOf(),
                isProductListLoading = false,
                onProductClick = {},
                onShoppingCartClicked = {},
                errors = listOf(UiText.DynamicString("This is a preview error"))
            )
        }
    }
}