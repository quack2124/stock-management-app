package com.app.stockmanagement.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class TransactionWIthProductEntity(
    @Embedded
    val transactionEntity: TransactionEntity,
    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val productEntity: ProductEntity
)