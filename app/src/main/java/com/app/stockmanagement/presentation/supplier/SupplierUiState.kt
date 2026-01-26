package com.app.stockmanagement.presentation.supplier

import com.app.stockmanagement.domain.model.Supplier

data class SupplierUiState(
    val isLoading: Boolean = false,
    val suppliers: List<Supplier> = emptyList()
)
