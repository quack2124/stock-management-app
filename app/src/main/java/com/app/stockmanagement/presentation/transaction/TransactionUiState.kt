package com.app.stockmanagement.presentation.transaction

import com.app.stockmanagement.domain.model.TransactionWithProduct

data class TransactionUiState(
    val isLoading: Boolean = false,
    val transactions: List<TransactionWithProduct> = emptyList()
)