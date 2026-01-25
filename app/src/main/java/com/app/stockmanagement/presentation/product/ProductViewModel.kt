package com.app.stockmanagement.presentation.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stockmanagement.data.mapper.toDomain
import com.app.stockmanagement.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUIState())
    val uiState: StateFlow<ProductUIState> = _uiState

    init {
        getAllProducts()
    }

    private fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.getAllProductsWithSupplier().collect {
                _uiState.value =
                    _uiState.value.copy(products = it.map { entity -> entity.toDomain() })
            }
        }
    }

    fun search(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (name.isEmpty()) {
                getAllProducts()
            } else {
                productRepository.searchForProductsByName(name).collect {
                    _uiState.value = _uiState.value.copy(
                        products = it.map { entity -> entity.toDomain() },
                    )
                }
            }

        }
    }


}