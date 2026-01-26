package com.app.stockmanagement.data.mapper

import com.app.stockmanagement.data.local.entity.TransactionWIthProductEntity
import com.app.stockmanagement.domain.model.TransactionWithProduct

fun TransactionWIthProductEntity.toDomain(): TransactionWithProduct = TransactionWithProduct(
    id = transactionEntity.id,
    date = transactionEntity.date,
    type = transactionEntity.type,
    quantity = transactionEntity.quantity,
    notes = transactionEntity.notes,
    productName = productEntity.name,
    productPrice = productEntity.price,
    productBarcode = productEntity.barcode,
    productId = transactionEntity.productId
)