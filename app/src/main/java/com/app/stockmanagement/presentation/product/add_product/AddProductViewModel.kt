package com.app.stockmanagement.presentation.product.add_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stockmanagement.data.repository.ProductRepository
import com.app.stockmanagement.data.repository.SupplierRepository
import com.app.stockmanagement.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val supplierRepository: SupplierRepository
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(AddProductUiState())
    val uiState: StateFlow<AddProductUiState> = _uiState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            supplierRepository.getAllSuppliers().collect {
                _uiState.value = _uiState.value.copy(suppliers = it)
            }
        }
    }

    fun addNewProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addProduct(product)
            _uiState.value = _uiState.value.copy(isSuccessful = true)
        }
    }

    fun getSuppliers() {

    }
}