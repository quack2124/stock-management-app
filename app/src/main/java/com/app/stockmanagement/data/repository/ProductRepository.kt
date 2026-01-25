package com.app.stockmanagement.data.repository

import com.app.stockmanagement.data.local.dao.ProductDao
import com.app.stockmanagement.domain.model.Product
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productDao: ProductDao
) {

    fun getAllProducts() = productDao.getAllProducts()

    suspend fun addProduct(product: Product) = productDao.addProduct(product)
}