package com.app.stockmanagement.presentation.product

import com.app.stockmanagement.domain.model.ProductWithSupplier

data class ProductUIState(
    val products: List<ProductWithSupplier> = emptyList()
)