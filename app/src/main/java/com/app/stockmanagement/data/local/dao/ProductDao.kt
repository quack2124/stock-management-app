package com.app.stockmanagement.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.app.stockmanagement.data.local.entity.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<Product>>
}