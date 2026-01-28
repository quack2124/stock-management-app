package com.app.stockmanagement.data.repository


import com.app.stockmanagement.data.local.dao.TransactionDao
import com.app.stockmanagement.data.local.entity.ProductEntity
import com.app.stockmanagement.data.local.entity.TransactionEntity
import com.app.stockmanagement.data.local.entity.TransactionWIthProductEntity
import com.app.stockmanagement.data.local.entity.Type
import com.app.stockmanagement.data.mapper.toDomain
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Date

class TransactionRepositoryImplTest {

    private val transactionDao: TransactionDao = mockk()
    private lateinit var repository: TransactionRepositoryImpl
    private lateinit var mockTransactionWithProductEntity: TransactionWIthProductEntity

    @Before
    fun setup() {
        repository = TransactionRepositoryImpl(transactionDao)
        mockTransactionWithProductEntity = TransactionWIthProductEntity(
            transactionEntity = TransactionEntity(
                id = 1,
                date = Date(),
                type = Type.SALE,
                productId = 1,
                quantity = 11,
                notes = "Big sale"
            ),
            productEntity = ProductEntity(
                id = 1,
                name = "Twix",
                description = "tasty chocolate",
                price = 2.0,
                category = "Food",
                barcode = "1231231",
                supplierId = 1,
                currentStockLevel = 11,
                minimumStockLevel = 11
            )
        )
    }

    @Test
    fun `getAllTransactionsWithProduct calls dao getAllTransactionsWithProduct once and should return correct result`(): Unit =
        runTest {
            // GIVEN
            val expectedLength = 1
            every { transactionDao.getAllTransactionsWithProduct() } returns flowOf(
                listOf(
                    mockTransactionWithProductEntity
                )
            )

            // WHEN
            val actual = repository.getAllTransactionsWithProduct().first()

            // THEN
            assertEquals(expectedLength, actual.size)
            assertEquals(
                mockTransactionWithProductEntity.transactionEntity.id,
                actual[0].transactionEntity.id
            )
            assertEquals(
                mockTransactionWithProductEntity.productEntity.id, actual[0].transactionEntity.id
            )
            verify(exactly = 1) { transactionDao.getAllTransactionsWithProduct() }
        }

    @Test
    fun `getAllTransactionsInLastHrs calls dao getAllTransactionsInLastHrs once and should return correct result`(): Unit =
        runTest {
            // GIVEN
            val expectedLength = 1
            val expectedDate = Date()
            every { transactionDao.getAllTransactionsInLastHrs(any()) } returns flowOf(
                listOf(
                    mockTransactionWithProductEntity
                )
            )

            // WHEN
            val actual = repository.getAllTransactionsInLastHrs(expectedDate).first()

            // THEN
            assertEquals(expectedLength, actual.size)
            assertEquals(
                mockTransactionWithProductEntity.transactionEntity.id,
                actual[0].transactionEntity.id
            )
            assertEquals(
                mockTransactionWithProductEntity.productEntity.id, actual[0].transactionEntity.id
            )
            verify(exactly = 1) { transactionDao.getAllTransactionsInLastHrs(expectedDate) }
        }

    @Test
    fun `addTransaction calls dao addTransaction once with correct params`(): Unit = runTest {
        // GIVEN
        val mockedTransaction = mockTransactionWithProductEntity.toDomain()
        coEvery { transactionDao.addTransaction(any()) } returns Unit

        // WHEN
        repository.addTransaction(mockedTransaction)

        // THEN
        coVerify(exactly = 1) { transactionDao.addTransaction(mockedTransaction) }

    }

}