package com.app.stockmanagement.presentation.transaction.transaction_management

sealed class TransactionManagementUiEffect {
    data class ShowError(val message: String) : TransactionManagementUiEffect()
    data object Success : TransactionManagementUiEffect()
}