package com.app.stockmanagement.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.stockmanagement.data.local.dao.ProductDao
import com.app.stockmanagement.data.local.dao.SupplierDao
import com.app.stockmanagement.data.local.entity.ProductEntity
import com.app.stockmanagement.data.local.entity.SupplierEntity
import com.app.stockmanagement.data.local.entity.TransactionEntity
import com.app.stockmanagement.util.DateConverters

@Database(
    entities = [ProductEntity::class, SupplierEntity::class, TransactionEntity::class],
    version = 1,
)
@TypeConverters(DateConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun supplierDao(): SupplierDao
}