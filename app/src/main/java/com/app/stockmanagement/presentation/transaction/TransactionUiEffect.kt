package com.app.stockmanagement.presentation.transaction

sealed class TransactionUiEffect {
    data class ShowError(val message: String) : TransactionUiEffect()
}