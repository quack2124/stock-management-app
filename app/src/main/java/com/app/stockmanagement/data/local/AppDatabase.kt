package com.app.stockmanagement.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.stockmanagement.data.local.entity.Product
import com.app.stockmanagement.data.local.entity.Supplier
import com.app.stockmanagement.data.local.entity.Transaction

@Database(
    entities = [Product::class, Supplier::class, Transaction::class],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {

}