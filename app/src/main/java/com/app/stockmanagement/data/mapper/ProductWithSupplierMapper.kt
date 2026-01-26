package com.app.stockmanagement.data.mapper

import com.app.stockmanagement.data.local.entity.ProductWithSupplierEntity
import com.app.stockmanagement.domain.model.ProductWithSupplier

fun ProductWithSupplierEntity.toDomain(): ProductWithSupplier = ProductWithSupplier(
    id = product.id,
    name = product.name,
    description = product.description,
    price = product.price,
    category = product.category,
    barcode = product.barcode,
    currentStockLevel = product.currentStockLevel,
    minimumStockLevel = product.minimumStockLevel,
    supplierName = supplier.name
)