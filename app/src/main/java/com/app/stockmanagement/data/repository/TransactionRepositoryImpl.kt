package com.app.stockmanagement.data.repository

import com.app.stockmanagement.data.local.dao.TransactionDao
import com.app.stockmanagement.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(private val transactionDao: TransactionDao) :
    TransactionRepository {

    override fun getAllTransactionsWithProduct() = transactionDao.getAllTransactionsWithProduct()
}