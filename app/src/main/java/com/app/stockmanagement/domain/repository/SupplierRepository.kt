package com.app.stockmanagement.domain.repository

import com.app.stockmanagement.data.local.entity.Supplier
import kotlinx.coroutines.flow.Flow

interface SupplierRepository {
    fun getAllSuppliers(): Flow<List<Supplier>>
}