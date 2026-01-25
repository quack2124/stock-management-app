package com.app.stockmanagement.domain.model

data class ProductWithSupplier(
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val barcode: String,
    val currentStockLevel: Int,
    val minimumStockLevel: Int,
    val supplierName: String,
)