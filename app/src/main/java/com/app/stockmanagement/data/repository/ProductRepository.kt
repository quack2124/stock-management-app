package com.app.stockmanagement.data.repository

import com.app.stockmanagement.data.local.dao.ProductDao
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productDao: ProductDao
) {

    fun getAllProducts() = productDao.getAllProducts()

}