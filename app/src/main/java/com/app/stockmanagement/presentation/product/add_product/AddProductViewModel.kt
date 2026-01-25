package com.app.stockmanagement.presentation.product.add_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stockmanagement.data.repository.ProductRepository
import com.app.stockmanagement.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(private val repository: ProductRepository) :
    ViewModel() {

    fun addNewProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addProduct(product)
        }
    }

}