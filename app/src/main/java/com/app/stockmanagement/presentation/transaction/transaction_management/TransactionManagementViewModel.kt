package com.app.stockmanagement.presentation.transaction.transaction_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stockmanagement.data.local.entity.Type
import com.app.stockmanagement.data.mapper.toDomain
import com.app.stockmanagement.domain.model.ProductWithSupplier
import com.app.stockmanagement.domain.model.TransactionWithProduct
import com.app.stockmanagement.domain.repository.ProductRepository
import com.app.stockmanagement.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionManagementViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val repository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionManagementUiState())
    val uiState: StateFlow<TransactionManagementUiState> = _uiState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            productRepository.getAllProductsWithSupplier().collect { products ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    products = products.map { it.toDomain() })

            }
        }
    }

    fun addTransaction(transaction: TransactionWithProduct, productToUpdate: ProductWithSupplier) {
        val updatedProduct = if (transaction.type == Type.SALE) {
            productToUpdate.copy(currentStockLevel = productToUpdate.currentStockLevel - transaction.quantity)
        } else {
            productToUpdate.copy(currentStockLevel = productToUpdate.currentStockLevel + transaction.quantity)

        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTransaction(transaction)
            productRepository.updateProduct(updatedProduct)
        }
    }

}