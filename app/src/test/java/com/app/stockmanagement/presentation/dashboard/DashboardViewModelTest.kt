package com.app.stockmanagement.presentation.dashboard

import com.app.stockmanagement.data.local.entity.ProductEntity
import com.app.stockmanagement.data.local.entity.ProductWithSupplierEntity
import com.app.stockmanagement.data.local.entity.SupplierEntity
import com.app.stockmanagement.data.mapper.toDomain
import com.app.stockmanagement.domain.repository.ProductRepository
import com.app.stockmanagement.domain.repository.TransactionRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private val transactionRepository: TransactionRepository = mockk()
    private val productRepository: ProductRepository = mockk()
    private lateinit var viewModel: DashboardViewModel
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher
        viewModel = DashboardViewModel(transactionRepository, productRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(Dispatchers::class)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getLowStockProducts should update state to success after loading`() = runTest {
        // 1. Prepare Mock Data
        mockkStatic(Dispatchers::class)
        // Force Dispatchers.IO to return our UnconfinedTestDispatcher
        every { Dispatchers.IO } returns UnconfinedTestDispatcher(testScheduler)
        val mockProductsWithSupplierEntity = listOf(
            ProductWithSupplierEntity(
                supplierEntity = SupplierEntity(
                    id = 1,
                    name = "Global supplier",
                    contactPerson = "Jane Peterson",
                    phone = "+44453021",
                    email = "jane@test.com",
                    address = "Example street 1"
                ),
                product = ProductEntity(
                    id = 1,
                    name = "Twix",
                    description = "Nice chocoalate",
                    price = 11.0,
                    category = "Food",
                    barcode = "1234567890",
                    supplierId = 1,
                    currentStockLevel = 11,
                    minimumStockLevel = 11
                ),
            )
        )
        val mockProductsWithSupplier = mockProductsWithSupplierEntity.map { it.toDomain() }
        coEvery {
            productRepository.getProductsWithLowStock(any())
        } returns flow {
            delay(10)
            emit(mockProductsWithSupplierEntity)
        }

        val states = mutableListOf<DashboardFragmentUiState>()

        // 2. Start collecting the StateFlow in the background
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect { states.add(it) }
        }

        viewModel.getLowStockProducts()
        advanceUntilIdle()
        assertEquals(3, states.size)
        assertTrue(states[1].isLoading)
        assertFalse(states[2].isLoading)
        assertEquals(mockProductsWithSupplier, states[2].products)
        unmockkStatic(Dispatchers::class)
    }
}

