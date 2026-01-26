package com.app.stockmanagement.domain.model

import android.os.Parcelable
import androidx.room.Ignore
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductWithSupplier(
    val id: Long,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val barcode: String,
    val currentStockLevel: Int,
    val minimumStockLevel: Int,
    @Ignore
    val supplierName: String,
) : Parcelable