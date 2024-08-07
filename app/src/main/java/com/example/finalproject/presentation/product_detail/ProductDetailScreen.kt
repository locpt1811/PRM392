package com.example.finalproject.presentation.product_detail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.finalproject.R
import com.example.finalproject.presentation.designsystem.components.ShoppingScaffold
import com.example.finalproject.presentation.designsystem.components.ShoppingShowToastMessage
import com.example.finalproject.presentation.designsystem.theme.ShoppingAppTheme
import com.example.finalproject.presentation.home.product.CategoryItem
import com.example.finalproject.utils.CustomPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onCartClick: () -> Unit,
    onChatClick: (String, String) -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.userMessages.isNotEmpty()) {
        ShoppingShowToastMessage(message = uiState.userMessages.first().asString())
        viewModel.consumedUserMessages()
    }

    if (uiState.errorMessages.isNotEmpty()) {
        ShoppingShowToastMessage(message = uiState.errorMessages.first().asString())
        viewModel.consumedErrorMessages()
    }

    ShoppingScaffold(modifier = modifier) { paddingValues ->
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.products),
                    color = colorResource(id = R.color.white)
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = colorResource(id = R.color.white))
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        )

        ProductDetailScreenContent(
            modifier = Modifier.padding(paddingValues),
            title = uiState.product?.title ?: "",
            description = uiState.product?.description ?: "",
            price = uiState.product?.price ?: 0.0,
            rate = uiState.product?.rating ?: 0.0,
            cateName = uiState.product?.category?.category_name ?: "",
            pubName = uiState.product?.publisher?.publisher_name ?: "",
            langName = uiState.product?.book_language?.language_name ?: "",
            langCode = uiState.product?.book_language?.language_code ?: "",
            image = uiState.product?.image_url ?: "",
            isProductFavorite = uiState.isProductFavorite,
            onFavoriteBtnClicked = viewModel::onFavoriteProductClick,
            onCategoryClick = viewModel::onCategoryClick,
            onAddToCartClicked = remember {
                {
                    if (uiState.isProductInCart) {
                        onCartClick()
                    } else {
                        viewModel.addProductToCart()
                    }
                }
            },
            onNavigateToChat = {
                val userId = uiState.userId // Assuming userId is stored in uiState
                val otherUserId = uiState.product?.user_id
                if (otherUserId != null && userId != null) {
                    onChatClick(userId, otherUserId)
                }
            },
            cartButtonText = if (uiState.isProductInCart) {
                stringResource(id = R.string.go_to_cart)
            } else {
                stringResource(id = R.string.add_to_cart)
            }
        )
    }
}

@Composable
private fun ProductDetailScreenContent(
    modifier: Modifier,
    title: String,
    description: String,
    price: Double,
    rate: Double,
    cateName: String,
    pubName:String,
    langName:String,
    langCode:String,
    image: String,
    isProductFavorite: Boolean,
    onFavoriteBtnClicked: () -> Unit,
    onAddToCartClicked: () -> Unit,
    onCategoryClick: (String) -> Unit,
    cartButtonText: String,
    onNavigateToChat: () -> Unit
) {
    Column(modifier = modifier
        .fillMaxSize()
    ) {
        AsyncImage(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.nine_level_margin)),
            model = ImageRequest.Builder(LocalContext.current).data(image)
                .crossfade(true).build(),
            contentDescription = null,
            error = painterResource(id = R.drawable.img_notfound),
            contentScale = ContentScale.Fit,
            placeholder = if (LocalInspectionMode.current) painterResource(id = R.drawable.debug_placeholder) else null
        )

        ProductDetails(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            title = title,
            description = description,
            price = price,
            rate = rate,
            cateName = cateName,
            pubName = pubName,
            langName = langName,
            langCode = langCode,
            isProductFavorite = isProductFavorite,
            onFavoriteBtnClicked = onFavoriteBtnClicked,
            onCategoryClick = onCategoryClick,
            onAddToCartClicked = onAddToCartClicked,
            cartButtonText = cartButtonText,
            onNavigateToChat = onNavigateToChat
        )
    }
}

@Composable
private fun ProductDetails(
    modifier: Modifier,
    title: String,
    description: String,
    price: Double,
    rate: Double,
    cateName: String,
    pubName:String,
    langName:String,
    langCode:String,
    onFavoriteBtnClicked: () -> Unit,
    onCategoryClick: (String) -> Unit,
    isProductFavorite: Boolean,
    onAddToCartClicked: () -> Unit,
    cartButtonText: String,
    onNavigateToChat: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin))
    ) {
        ProductInfo(
            modifier = Modifier.weight(4f),
            title = title,
            description = description,
            rate = rate,
            cateName = cateName,
            pubName = pubName,
            langName = langName,
            langCode = langCode,
            onFavoriteBtnClicked = onFavoriteBtnClicked,
            isProductFavorite = isProductFavorite,
            onCategoryClick = onCategoryClick,
            onNavigateToChat = onNavigateToChat,
        )
        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.Black)

        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.two_level_margin)),
                text = "$$price",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(id = R.dimen.four_level_margin),
                        end = dimensionResource(id = R.dimen.two_level_margin)
                    ),
                onClick = onAddToCartClicked,
                contentPadding = PaddingValues(vertical = 12.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = cartButtonText,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Composable
private fun ProductInfo(
    modifier: Modifier,
    title: String,
    description: String,
    rate: Double,
    cateName: String,
    pubName:String,
    langName:String,
    langCode:String,
    isProductFavorite: Boolean,
    onFavoriteBtnClicked: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onNavigateToChat: () -> Unit
    ) {
    Column(modifier = modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = colorResource(id = R.color.orange)
                )
                Text(text = "$rate")
            }
            Row {
                // Chat button
                IconButton(onClick = onNavigateToChat) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Message,
                        contentDescription = "Navigate to Chat",
                        tint = colorResource(id = R.color.black)
                    )
                }
                // Favorite button
                IconButton(onClick = onFavoriteBtnClicked) {
                    Icon(
                        imageVector = if (isProductFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = null,
                        tint = Color.Red
                    )
                }
            }
        }

        Column(modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
        ){
            Text(
                modifier = Modifier
                    .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin))
                    .padding(top = dimensionResource(id = R.dimen.one_level_margin)),
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Row(modifier = Modifier.padding(
                horizontal = dimensionResource(id = R.dimen.two_level_margin),
            )){
                CategoryItem(
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin)),
                    categoryName = cateName,
                    selectedCatName = cateName,
                    onCategoryClick = onCategoryClick
                )
            }

            Row(
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.two_level_margin),
                )
            ){
                Text(
                    text = "Publisher:", fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = pubName
                )
            }

            Row(
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.two_level_margin),
                )
            ){
                Text(
                    text = "Code:", fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = langCode
                )
            }

            Row(
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.two_level_margin),
                )
            ){
                Text(
                    text = "Language:", fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = langName
                )
            }

            var seeMore by remember { mutableStateOf(true) }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin))
                    .padding(top = dimensionResource(id = R.dimen.one_level_margin)),
                text = description,
                maxLines = if (seeMore) 5 else Int.MAX_VALUE,
                overflow = TextOverflow.Ellipsis,
            )
            val textButton = if (seeMore) {
                stringResource(id = R.string.see_more)
            } else {
                stringResource(id = R.string.see_less)
            }
            Text(
                text = textButton,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .heightIn(20.dp)
                    .fillMaxWidth()
                    .padding(top = 15.dp)
                    .clickable {
                        seeMore = !seeMore
                    }
            )
        }

    }
}

@CustomPreview
@Composable
private fun ProductDetailScreenPreview() {
    ShoppingAppTheme {
        Surface {
            ProductDetailScreenContent(
                modifier = Modifier,
                title = "Product Title",
                description = previewDescription,
                price = 120.0,
                rate = 4.3,
                image = "",
                cateName = "Children",
                pubName = "Shogakukan",
                langName = "United Stated English",
                langCode = "en-us",
                isProductFavorite = false,
                onFavoriteBtnClicked = {},
                onAddToCartClicked = {},
                onCategoryClick = {},
                cartButtonText = "Add to Cart",
                onNavigateToChat = {}
            )
        }
    }
}

@CustomPreview
@Composable
private fun ProductDetailScreenProductFavoritePreview() {
    ShoppingAppTheme {
        Surface {
            ProductDetailScreenContent(
                modifier = Modifier,
                title = "Product Title",
                description = previewDescription,
                price = 120.0,
                rate = 4.3,
                image = "",
                cateName = "Children",
                pubName = "Shogakukan",
                langName = "United Stated English",
                langCode = "en-us",
                isProductFavorite = true,
                onFavoriteBtnClicked = {},
                onAddToCartClicked = {},
                onCategoryClick = {},
                cartButtonText = "Go to Cart",
                onNavigateToChat = {}
            )
        }
    }
}

private const val previewDescription = "Ranranrn " +
        "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, " +
        "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."+
        "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."+
        "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."