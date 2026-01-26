package com.app.stockmanagement.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ProductWithSupplierEntity(
    @Embedded
    val product: ProductEntity,
    @Relation(
        parentColumn = "supplierId", // The foreign key column in Product
        entityColumn = "id"          // The primary key column in Category
    )
    val supplierEntity: SupplierEntity
)