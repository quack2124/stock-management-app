package com.app.stockmanagement.domain.model

import androidx.room.Ignore
import com.app.stockmanagement.data.local.entity.Type
import java.util.Date

data class TransactionWithProduct(
    val id: Long,
    val date: Date,
    val type: Type,
    val quantity: Int,
    val notes: String?,
    val productId: Long,
    @Ignore
    val productName: String = "",
    @Ignore
    val productPrice: Double = 0.0,
    @Ignore
    val productBarcode: String = "",
)