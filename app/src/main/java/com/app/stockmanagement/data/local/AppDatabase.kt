package com.app.stockmanagement.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.stockmanagement.data.local.dao.ProductDao
import com.app.stockmanagement.data.local.dao.SupplierDao
import com.app.stockmanagement.data.local.entity.ProductEntity
import com.app.stockmanagement.data.local.entity.Supplier
import com.app.stockmanagement.data.local.entity.Transaction

@Database(
    entities = [ProductEntity::class, Supplier::class, Transaction::class],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun supplierDao(): SupplierDao
}