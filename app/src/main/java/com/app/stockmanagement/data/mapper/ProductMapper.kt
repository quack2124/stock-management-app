package com.app.stockmanagement.data.mapper

import com.app.stockmanagement.data.local.entity.ProductEntity
import com.app.stockmanagement.domain.model.Product

//fun PostDto.toEntity(): PostEntity = ProductEntity(
//    id = id,
//    name = name,
//    description = description,
//    price = price,
//    category = category,
//    barcode = barcode,
//    supplierId = supplierId,
//    currentStockLevel = currentStockLevel,
//    minimumStockLevel = minimumStockLevel
//)
//
fun ProductEntity.toDomain(): Product = Product(
    name = name,
    description = description,
    price = price,
    category = category,
    barcode = barcode,
    supplierId = supplierId,
    currentStockLevel = currentStockLevel,
    minimumStockLevel = minimumStockLevel
)