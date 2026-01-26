package com.app.stockmanagement.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.app.stockmanagement.data.local.entity.SupplierEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SupplierDao {

    @Query("SELECT * FROM suppliers")
    fun getAllSuppliers(): Flow<List<SupplierEntity>>

    @Query("SELECT * FROM suppliers WHERE name LIKE '%' || :name || '%'")
    fun searchForSupplierByName(name: String): Flow<List<SupplierEntity>>

}