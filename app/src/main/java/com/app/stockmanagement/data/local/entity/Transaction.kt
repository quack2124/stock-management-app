package com.app.stockmanagement.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val date: Long,
    val type: Type,
    val productId: Long,
    val quantity: Int,
    val notes: String?
)

enum class Type {
    RESTOCK, SALE
}