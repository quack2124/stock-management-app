package com.app.stockmanagement.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.app.stockmanagement.data.local.entity.TransactionEntity
import com.app.stockmanagement.data.local.entity.TransactionWIthProductEntity
import com.app.stockmanagement.domain.model.TransactionWithProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Transaction
    @Query("SELECT * FROM transactions")
    fun getAllTransactionsWithProduct(): Flow<List<TransactionWIthProductEntity>>

    @Insert(entity = TransactionEntity::class)
    suspend fun addTransaction(transaction: TransactionWithProduct)
}