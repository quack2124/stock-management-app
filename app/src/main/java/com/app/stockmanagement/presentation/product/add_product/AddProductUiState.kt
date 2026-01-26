package com.app.stockmanagement.presentation.product.add_product

import com.app.stockmanagement.domain.model.Supplier

data class AddProductUiState(
    val suppliers: List<Supplier> = emptyList(),
    val isSuccessful: Boolean = false
)