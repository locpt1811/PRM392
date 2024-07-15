package com.example.finalproject.presentation.home.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.common.Response
import com.example.finalproject.common.helper.PreferenceManager
import com.example.finalproject.common.helper.UiText
import com.example.finalproject.domain.repository.BookRepository
import com.example.finalproject.model.shopping.BookEntity
import com.example.finalproject.utils.SEARCH_HISTORY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: BookRepository,
    private val ioDispatcher: CoroutineDispatcher,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchScreenUiState())
    val uiState: StateFlow<SearchScreenUiState> = _uiState.asStateFlow()

    var searchedText by mutableStateOf("")

    init {
        getAllProducts()
    }

    fun onSearchValueChange(value: String) {
        searchedText = value

        viewModelScope.launch {
            val searchedResults = if (searchedText.isNotBlank()) {
                _uiState.value.productList.filter {
                    it.title?.contains(searchedText, ignoreCase = true) == true
                }
            } else {
                emptyList()
            }

            _uiState.value = _uiState.value.copy(
                searchResult = searchedResults,
                isSearchResultEmpty = searchedResults.isEmpty() && searchedText.isNotBlank()
            )
        }
    }

    fun onSearchValueTopBarChange(value: String) {
        searchedText = value

        viewModelScope.launch {
            val searchedResults = if (searchedText.isNotBlank()) {
                _uiState.value.productList.filter {
                    it.title?.contains(searchedText, ignoreCase = true) == true
                }
            } else {
                emptyList()
            }

            _uiState.value = _uiState.value.copy(
                searchResult = searchedResults,
                isSearchResultEmpty = searchedResults.isEmpty() && searchedText.isNotBlank()
            )
        }
    }

    fun setQueryToEmpty() {
        searchedText = ""
    }
    fun resetSearch() {
        searchedText = ""

        _uiState.update {
            it.copy(
                searchResult = listOf(),
                isSearchResultEmpty = true
            )
        }
    }

    private fun getAllProducts() {
        viewModelScope.launch(ioDispatcher) {
            when (val response = repository.getAllBookDb()) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(productList = response.data)
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

    fun searchTitle(value: String) {
        searchedText = value

        viewModelScope.launch(ioDispatcher) {
            when (val response = repository.getAllBookDbByTitle(value)) {
                is Response.Success -> {
                    _uiState.value = _uiState.value.copy(
                        searchResult = response.data,
                        isSearchResultEmpty = response.data.isEmpty() && searchedText.isNotBlank()
                    )
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

    fun errorConsumed() {
        _uiState.update {
            it.copy(errorMessages = listOf())
        }
    }
}

data class SearchScreenUiState(
    val productList: List<BookEntity> = listOf(),
    val searchResult: List<BookEntity> = listOf(),
    val errorMessages: List<UiText> = listOf(),
    val isSearchResultEmpty: Boolean = false
)