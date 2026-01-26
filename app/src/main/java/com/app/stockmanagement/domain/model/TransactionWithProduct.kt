package com.app.stockmanagement.domain.model

import com.app.stockmanagement.data.local.entity.Type
import java.util.Date

data class TransactionWithProduct(
    val id: Long,
    val date: Date,
    val type: Type,
    val quantity: Int,
    val notes: String?,
    val productName: String,
    val productPrice: Double,
    val productBarcode: String,

    )