package com.app.stockmanagement.presentation.product.edit_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stockmanagement.domain.model.ProductWithSupplier
import com.app.stockmanagement.domain.repository.ProductRepository
import com.app.stockmanagement.util.Constants.ERROR
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProductViewModel @Inject constructor(private val repository: ProductRepository) :
    ViewModel() {
    private val _eventFlow = MutableSharedFlow<EditProductEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun updateProduct(product: ProductWithSupplier) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateProduct(product)
                _eventFlow.emit(EditProductEvent.Success)

            } catch (er: Exception) {
                _eventFlow.emit(EditProductEvent.ShowError(ERROR))
            }
        }
    }
}