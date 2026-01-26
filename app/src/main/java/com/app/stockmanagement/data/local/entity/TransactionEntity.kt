package com.app.stockmanagement.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val date: Date,
    val type: Type,
    val productId: Long,
    val quantity: Int,
    val notes: String?
)

enum class Type {
    RESTOCK, SALE
}