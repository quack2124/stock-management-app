package com.app.stockmanagement.presentation.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stockmanagement.data.local.entity.ProductWithSupplierEntity
import com.app.stockmanagement.data.mapper.toDomain
import com.app.stockmanagement.domain.repository.ProductRepository
import com.app.stockmanagement.domain.usecase.GetAllProductsUseCase
import com.app.stockmanagement.util.UseCaseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUIState())
    val uiState: StateFlow<ProductUIState> = _uiState

    init {
        getAllProducts()
    }

    private fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getAllProductsUseCase(object : UseCaseHandler<List<ProductWithSupplierEntity>> {
                override fun onSuccess(result: List<ProductWithSupplierEntity>) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        cachedProducts = result.map { entity -> entity.toDomain() },
                        products = result.map { it.toDomain() })
                }

                override fun onFailure() {

                }
            })
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

    fun filterResults(category: String, viewId: Int) {
        val filtered = if (category == ALL_ITEMS) {
            _uiState.value.cachedProducts

        } else {
            _uiState.value.cachedProducts.filter { it.category == category }
        }

        _uiState.value = _uiState.value.copy(
            products = filtered,
            checkedElement = viewId
        )
    }

    companion object {
        const val ALL_ITEMS = "All Items"
    }
}