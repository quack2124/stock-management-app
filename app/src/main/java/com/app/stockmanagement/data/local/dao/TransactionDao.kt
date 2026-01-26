package com.app.stockmanagement.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.app.stockmanagement.data.local.entity.TransactionWIthProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Transaction
    @Query("SELECT * FROM transactions")
    fun getAllTransactionsWithProduct(): Flow<List<TransactionWIthProductEntity>>
}