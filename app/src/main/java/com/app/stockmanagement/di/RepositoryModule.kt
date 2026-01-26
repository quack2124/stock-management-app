package com.app.stockmanagement.di

import com.app.stockmanagement.data.repository.ProductRepositoryImpl
import com.app.stockmanagement.data.repository.SupplierRepositoryImpl
import com.app.stockmanagement.data.repository.TransactionRepositoryImpl
import com.app.stockmanagement.domain.repository.ProductRepository
import com.app.stockmanagement.domain.repository.SupplierRepository
import com.app.stockmanagement.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository

    @Binds
    abstract fun bindSupplierRepository(impl: SupplierRepositoryImpl): SupplierRepository

    @Binds
    abstract fun bindTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository
}