package com.app.stockmanagement.di

import android.content.Context
import androidx.room.Room
import com.app.stockmanagement.data.local.AppDatabase
import com.app.stockmanagement.data.local.dao.ProductDao
import com.app.stockmanagement.data.local.dao.SupplierDao
import com.app.stockmanagement.data.local.dao.TransactionDao
import com.app.stockmanagement.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, Constants.DB_NAME).build()

    @Provides
    fun provideProductDao(db: AppDatabase): ProductDao = db.productDao()

    @Provides
    fun provideSupplier(db: AppDatabase): SupplierDao = db.supplierDao()

    @Provides
    fun provideTransactionDao(db: AppDatabase): TransactionDao = db.transactionDao()
}