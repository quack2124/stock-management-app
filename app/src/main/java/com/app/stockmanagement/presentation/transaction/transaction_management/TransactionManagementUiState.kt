package com.app.stockmanagement.presentation.transaction.transaction_management

import com.app.stockmanagement.domain.model.ProductWithSupplier

data class TransactionManagementUiState(
    val isLoading: Boolean = false,
    val products: List<ProductWithSupplier> = emptyList()
)