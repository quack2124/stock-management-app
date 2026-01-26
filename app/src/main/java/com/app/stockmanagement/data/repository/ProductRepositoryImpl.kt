package com.app.stockmanagement.data.repository

import com.app.stockmanagement.data.local.dao.ProductDao
import com.app.stockmanagement.domain.model.Product
import com.app.stockmanagement.domain.model.ProductWithSupplier
import com.app.stockmanagement.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao
) : ProductRepository {

    override fun getAllProductsWithSupplier() = productDao.getAllProductsWithSupplier()

    override fun searchForProductsByName(name: String) = productDao.searchForProductsByName(name)

    override suspend fun addProduct(product: Product) = productDao.addProduct(product)

    override suspend fun updateProduct(product: ProductWithSupplier) =
        productDao.updateProduct(product)
}