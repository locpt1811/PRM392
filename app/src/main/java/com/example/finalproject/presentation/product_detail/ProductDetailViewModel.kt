package com.example.finalproject.presentation.product_detail

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.R
import com.example.finalproject.common.Response
import com.example.finalproject.common.helper.UiText
import com.example.finalproject.data.mapper.toProductEntity
import com.example.finalproject.domain.repository.AuthRepository
import com.example.finalproject.domain.repository.BookRepository
import com.example.finalproject.domain.repository.ChatRepository
import com.example.finalproject.model.shopping.BookDTO
import com.example.finalproject.model.shopping.CartEntity
import com.example.finalproject.presentation.navigation.MainDestinations
import com.example.finalproject.presentation.navigation.ShoppingAppNavController
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@Stable
@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val bookRepository: BookRepository,
    private val authRepository: AuthRepository,
    private val chatRepository: ChatRepository,
    private val navController: ShoppingAppNavController,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductScreenUiState())
    val uiState: StateFlow<ProductScreenUiState> = _uiState.asStateFlow()

    init {
        savedStateHandle.get<String>(MainDestinations.PRODUCT_DETAIL_KEY)?.let { encodedArg ->
            try {
                val decodedValue = URLDecoder.decode(encodedArg.replace("%", "%25"), StandardCharsets.UTF_8.name())
                val product = Gson().fromJson(decodedValue, BookDTO::class.java)
                _uiState.update { it.copy(product = product) }
                findFavProduct(productId = product.book_id)
                findProductInCart(productId = product.book_id)
            } catch (e: Exception) {
                Log.e("ProductDetailViewModel", "Error decoding or parsing product data", e)
            }
        }
    }

    private fun findFavProduct(productId: Int) {
        viewModelScope.launch(ioDispatcher) {
            when (val response = bookRepository.findFavoriteProduct(productId)) {
                is Response.Success<*> -> {
                    _uiState.update {
                        it.copy(isProductFavorite = response.data != null)
                    }
                }

                is Response.Error<*> -> {
                    _uiState.update {
                        it.copy(errorMessages = listOf(
                            UiText.StringResource(response.errorMessageId)
                        ))
                    }
                }
            }
        }
    }

    fun onFavoriteProductClick() {
        if (_uiState.value.isProductFavorite) {
            removeProductFromFavorites()
        } else {
            addProductToFavorites()
        }
    }

    private fun addProductToFavorites() {
        viewModelScope.launch(ioDispatcher) {
            when (val response =
                _uiState.value.product?.toProductEntity()
                    ?.let { bookRepository.addFavoriteProduct(it) }
            ) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(
                            userMessages = listOf(
                                UiText.StringResource(R.string.product_added_favorites)
                            ),
                            isProductFavorite = true
                        )
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(errorMessages = listOf(
                            UiText.StringResource(response.errorMessageId)
                        ))
                    }
                }

                else -> {
                    _uiState.update {
                        it.copy(errorMessages = listOf(
                            UiText.StringResource(R.string.unknown_error)
                        ))
                    }
                }
            }
        }
    }

    private fun removeProductFromFavorites() {
        viewModelScope.launch(ioDispatcher) {
            when (val response =
                _uiState.value.product?.book_id?.let { bookRepository.removeFavoriteProduct(it) }
            ) {
                is Response.Success<*> -> {
                    _uiState.update {
                        it.copy(
                            userMessages = listOf(
                                UiText.StringResource(R.string.product_removed_favorites)
                            ),
                            isProductFavorite = false
                        )
                    }
                }

                is Response.Error<*> -> {
                    _uiState.update {
                        it.copy(errorMessages = listOf(
                            UiText.StringResource(response.errorMessageId)
                        ))
                    }
                }

                else -> {
                    _uiState.update {
                        it.copy(errorMessages = listOf(
                            UiText.StringResource(R.string.unknown_error)
                        ))
                    }
                }
            }
        }
    }

    fun addProductToCart() {
        val product = _uiState.value.product

        if (product?.book_id != null && product.title != null && product.price != null && product.image_url != null) {
            viewModelScope.launch(ioDispatcher) {
                when (val response = bookRepository.addProductToCart(
                    CartEntity(
                        id = product.book_id,
                        title = product.title,
                        price = product.price.toDouble(),
                        image = product.image_url,
                        count = 1
                    )
                )) {
                    is Response.Success<*> -> {
                        _uiState.update {
                            it.copy(
                                userMessages = listOf(
                                    UiText.StringResource(R.string.product_added_cart)
                                ),
                                isProductInCart = true
                            )
                        }
                    }
                    is Response.Error<*> -> {
                        _uiState.update {
                            it.copy(errorMessages = listOf(
                                UiText.StringResource(response.errorMessageId)
                            ))
                        }
                    }
                }
            }
        }
    }

    private fun findProductInCart(productId: Int) {
        viewModelScope.launch(ioDispatcher) {
            when (val response = bookRepository.findCartItem(productId)) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(isProductInCart = response.data != null)
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(errorMessages = listOf(
                            UiText.StringResource(response.errorMessageId)
                        ))
                    }
                }
            }
        }
    }
    fun navigateToChat() {
        viewModelScope.launch(ioDispatcher) {
            val currentUser = authRepository.retreiveCurrentUser()
            val productOwner = _uiState.value.product?.user_id ?: ""

            if (currentUser != null && productOwner.isNotEmpty()) {
                Log.e("ProductDetailViewModel", "USER "+currentUser.uid+ "OWNER "+ productOwner)

                val response = chatRepository.getChatRoomId(currentUser.uid, productOwner)
                if (response is Response.Success) {
                    val chatRoomId = response.data ?: ""
                    Log.e("ProductDetailViewModel", "NAVIGATE SUCCESS")
                    navController.navigateToChat(chatRoomId)

                } else {
                    // Handle error case
                    Log.e("ProductDetailViewModel", "Failed to retrieve chat room ID")
                }
            } else {
                Log.e("ProductDetailViewModel", "Current user or product owner is null or empty")
            }
        }
    }

    fun consumedUserMessages() {
        _uiState.update {
            it.copy(userMessages = listOf())
        }
    }

    fun consumedErrorMessages() {
        _uiState.update {
            it.copy(errorMessages = listOf())
        }
    }
}

data class ProductScreenUiState(
    val product: BookDTO? = null,
    val isProductFavorite: Boolean = false,
    val userMessages: List<UiText> = listOf(),
    val errorMessages: List<UiText> = listOf(),
    val isProductInCart: Boolean = false
)