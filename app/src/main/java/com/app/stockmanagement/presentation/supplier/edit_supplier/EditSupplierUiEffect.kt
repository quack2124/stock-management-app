package com.app.stockmanagement.presentation.supplier.edit_supplier

sealed class EditSupplierUiEffect {
    data class ShowError(val message: String) : EditSupplierUiEffect()
    data object Success : EditSupplierUiEffect()

}