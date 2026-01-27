package com.app.stockmanagement.domain.repository

import com.app.stockmanagement.data.local.entity.TransactionWIthProductEntity
import com.app.stockmanagement.domain.model.TransactionWithProduct
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface TransactionRepository {

    fun getAllTransactionsWithProduct(): Flow<List<TransactionWIthProductEntity>>

    fun getAllTransactionsInLastHrs(date: Date): Flow<List<TransactionWIthProductEntity>>

    suspend fun addTransaction(transaction: TransactionWithProduct)
}