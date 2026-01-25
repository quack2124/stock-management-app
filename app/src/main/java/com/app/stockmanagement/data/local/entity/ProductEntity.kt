package com.app.stockmanagement.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val barcode: String,
    val supplierId: Long,
    val currentStockLevel: Int,
    val minimumStockLevel: Int
)
