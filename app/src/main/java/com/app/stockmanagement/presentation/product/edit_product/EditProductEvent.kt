package com.app.stockmanagement.presentation.product.edit_product

sealed class EditProductEvent {
    data class ShowError(val message: String) : EditProductEvent()
    object Success : EditProductEvent()
}