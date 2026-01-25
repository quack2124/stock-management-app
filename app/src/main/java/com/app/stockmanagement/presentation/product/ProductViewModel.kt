package com.app.stockmanagement.presentation.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stockmanagement.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    val productRepository: ProductRepository
) : ViewModel() {

    fun getAllProducts() {
        viewModelScope.launch {
            productRepository.getAllProducts().collect {
                it
            }
        }
    }


}