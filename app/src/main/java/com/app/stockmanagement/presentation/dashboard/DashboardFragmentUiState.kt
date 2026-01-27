package com.app.stockmanagement.presentation.dashboard

import com.app.stockmanagement.domain.model.ProductWithSupplier
import com.app.stockmanagement.domain.model.TransactionWithProduct

data class DashboardFragmentUiState(
    val isLoading: Boolean = false,
    val transactions: List<TransactionWithProduct> = emptyList(),
    val products: List<ProductWithSupplier> = emptyList()

)