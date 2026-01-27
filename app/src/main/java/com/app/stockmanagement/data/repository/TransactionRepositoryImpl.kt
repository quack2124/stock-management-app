package com.app.stockmanagement.data.repository

import com.app.stockmanagement.data.local.dao.TransactionDao
import com.app.stockmanagement.data.local.entity.TransactionWIthProductEntity
import com.app.stockmanagement.domain.model.TransactionWithProduct
import com.app.stockmanagement.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(private val transactionDao: TransactionDao) :
    TransactionRepository {

    override fun getAllTransactionsWithProduct() = transactionDao.getAllTransactionsWithProduct()

    override fun getAllTransactionsInLastHrs(date: Date): Flow<List<TransactionWIthProductEntity>> =
        transactionDao.getAllTransactionsInLastHrs(date)

    override suspend fun addTransaction(transaction: TransactionWithProduct) =
        transactionDao.addTransaction(transaction)
}