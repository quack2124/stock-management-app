package com.app.stockmanagement.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.app.stockmanagement.data.local.entity.ProductEntity
import com.app.stockmanagement.data.local.entity.ProductWithSupplierEntity
import com.app.stockmanagement.domain.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Insert(entity = ProductEntity::class)
    suspend fun addProduct(product: Product)

    @Transaction
    @Query("SELECT * FROM products")
    fun getAllProductsWithSupplier(): Flow<List<ProductWithSupplierEntity>>

    @Transaction
    @Query("SELECT * FROM products WHERE name LIKE '%' || :name || '%'")
    fun searchForProductsByName(name: String): Flow<List<ProductWithSupplierEntity>>
}