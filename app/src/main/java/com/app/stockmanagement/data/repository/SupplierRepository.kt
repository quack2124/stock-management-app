package com.app.stockmanagement.data.repository

import com.app.stockmanagement.data.local.dao.SupplierDao
import javax.inject.Inject

class SupplierRepository @Inject constructor(
    private val supplierDao: SupplierDao
) {
    fun getAllSuppliers() = supplierDao.getAllSuppliers();
}