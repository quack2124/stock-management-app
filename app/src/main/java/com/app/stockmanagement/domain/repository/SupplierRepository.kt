package com.app.stockmanagement.domain.repository

import com.app.stockmanagement.data.local.entity.SupplierEntity
import kotlinx.coroutines.flow.Flow

interface SupplierRepository {
    fun getAllSuppliers(): Flow<List<SupplierEntity>>
}