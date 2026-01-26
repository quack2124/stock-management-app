package com.app.stockmanagement.data.repository

import com.app.stockmanagement.data.local.dao.SupplierDao
import com.app.stockmanagement.domain.model.Supplier
import com.app.stockmanagement.domain.repository.SupplierRepository
import javax.inject.Inject

class SupplierRepositoryImpl @Inject constructor(
    private val supplierDao: SupplierDao
) : SupplierRepository {
    override fun getAllSuppliers() = supplierDao.getAllSuppliers();

    override fun searchForSupplierByName(name: String) = supplierDao.searchForSupplierByName(name)

    override suspend fun updateSupplier(supplier: Supplier) = supplierDao.updateSupplier(supplier)
}