package com.app.stockmanagement.presentation.supplier.add_supplier

sealed class AddSupplierUiEffect {
    data class ShowError(val message: String) : AddSupplierUiEffect()
    data object Success : AddSupplierUiEffect()
}