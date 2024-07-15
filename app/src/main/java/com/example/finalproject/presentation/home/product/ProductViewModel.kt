package com.example.finalproject.presentation.home.product

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.common.Response
import com.example.finalproject.common.helper.UiText
import com.example.finalproject.domain.repository.AuthRepository
import com.example.finalproject.domain.repository.BookRepository
import com.example.finalproject.model.shopping.BookDTO
import com.example.finalproject.model.shopping.CateDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductScreenUiState(
    val categoryUiState: CategoryUiState = CategoryUiState(),
    val productUiState: ProductUiState = ProductUiState(),
    val errorMessages: List<UiText> = listOf()
)

data class CategoryUiState(
    val isLoading: Boolean = true,
    val categoryList: List<String> = listOf()
)

data class ProductUiState(
    val isLoading: Boolean = true,
    val productList: List<BookDTO> = listOf()
)

@Stable
@HiltViewModel
class ProductViewModel @Inject constructor(
    private val shoppingRepository: BookRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductScreenUiState())
    val uiState: StateFlow<ProductScreenUiState> = _uiState.asStateFlow()

    init {
        getCategoryList()
        getAllProducts()
//        getFCMTokenAndUpload()
    }

    private fun getCategoryList() {
        viewModelScope.launch(ioDispatcher) {
            when (val response = shoppingRepository.getCategories()) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(
                            categoryUiState = CategoryUiState(
                                isLoading = false,
                                categoryList = response.data.map { category ->
                                    category.category_name.toString().replaceFirstChar { char -> char.uppercase() }
                                }
                            )
                        )
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(
                            categoryUiState = CategoryUiState(isLoading = false),
                            errorMessages = listOf(
                                UiText.StringResource(resId = response.errorMessageId)
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getAllProducts() {
        viewModelScope.launch(ioDispatcher) {
            when (val response = shoppingRepository.getBooksFullDetail()) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(
                            productUiState = ProductUiState(
                                isLoading = false,
                                productList = response.data
                            )
                        )
                    }
//                    saveAllProductsToDb(response.data)
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(
                            productUiState = ProductUiState(isLoading = false),
                            errorMessages = listOf(
                                UiText.StringResource(resId = response.errorMessageId)
                            )
                        )
                    }
                }
            }
        }
    }

//    private fun saveAllProductsToDb(productList: List<Product>) {
//        viewModelScope.launch(ioDispatcher) {
//            productList.forEach {
//                shoppingRepository.addProduct(it.toProductEntity())
//            }
//        }
//    }

//    private fun getFCMTokenAndUpload() {
//        viewModelScope.launch(ioDispatcher) {
//            authRepository.getFCMToken().addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    authRepository.uploadUserFCMToken(token = task.result)
//                }
//            }
//        }
//    }
}