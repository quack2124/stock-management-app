package com.app.stockmanagement.domain.repository

import com.app.stockmanagement.data.local.entity.TransactionWIthProductEntity
import com.app.stockmanagement.domain.model.TransactionWithProduct
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    fun getAllTransactionsWithProduct(): Flow<List<TransactionWIthProductEntity>>

    suspend fun addTransaction(transaction: TransactionWithProduct)
}