package com.app.stockmanagement.domain.repository

import com.app.stockmanagement.data.local.entity.SupplierEntity
import com.app.stockmanagement.domain.model.Supplier
import kotlinx.coroutines.flow.Flow

interface SupplierRepository {
    fun getAllSuppliers(): Flow<List<SupplierEntity>>

    fun searchForSupplierByName(name: String): Flow<List<SupplierEntity>>

    suspend fun updateSupplier(supplier: Supplier)

    suspend fun addSupplier(supplier: Supplier)

}