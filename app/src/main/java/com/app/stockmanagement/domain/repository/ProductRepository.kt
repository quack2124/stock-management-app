package com.app.stockmanagement.domain.repository

import com.app.stockmanagement.data.local.entity.ProductWithSupplierEntity
import com.app.stockmanagement.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    fun getAllProductsWithSupplier(): Flow<List<ProductWithSupplierEntity>>

    fun searchForProductsByName(name: String): Flow<List<ProductWithSupplierEntity>>

    suspend fun addProduct(product: Product)
}