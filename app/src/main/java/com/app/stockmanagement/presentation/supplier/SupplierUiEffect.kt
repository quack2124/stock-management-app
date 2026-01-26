package com.app.stockmanagement.presentation.supplier

sealed class SupplierUiEffect {
    data class ShowError(val message: String) : SupplierUiEffect()
}