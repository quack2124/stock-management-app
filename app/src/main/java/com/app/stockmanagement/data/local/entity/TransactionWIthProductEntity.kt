package com.app.stockmanagement.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class TransactionWIthProductEntity(
    @Embedded
    val transaction: TransactionEntity,
    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val productEntity: ProductEntity
)