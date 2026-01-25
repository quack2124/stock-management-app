package com.app.stockmanagement.domain.model

data class Product(
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val barcode: String,
    val supplierId: Long,
    val currentStockLevel: Int,
    val minimumStockLevel: Int
)
