package com.app.stockmanagement.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.app.stockmanagement.data.local.entity.Supplier
import kotlinx.coroutines.flow.Flow

@Dao
interface SupplierDao {

    @Query("SELECT * FROM suppliers")
    fun getAllSuppliers(): Flow<List<Supplier>>

}