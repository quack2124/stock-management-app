package com.app.stockmanagement.data.repository

import com.app.stockmanagement.data.local.dao.ProductDao
import com.app.stockmanagement.domain.model.Product
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productDao: ProductDao
) {

    fun getAllProductsWithSupplier() = productDao.getAllProductsWithSupplier()

    fun searchForProductsByName(name: String) = productDao.searchForProductsByName(name)

    suspend fun addProduct(product: Product) = productDao.addProduct(product)
}