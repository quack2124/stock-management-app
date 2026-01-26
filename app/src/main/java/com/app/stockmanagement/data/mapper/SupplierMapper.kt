package com.app.stockmanagement.data.mapper

import com.app.stockmanagement.data.local.entity.SupplierEntity
import com.app.stockmanagement.domain.model.Supplier


fun SupplierEntity.toDomain(): Supplier = Supplier(
    id = id,
    name = name,
    contactPerson = contactPerson,
    phone = phone,
    email = email,
    address = address,
)