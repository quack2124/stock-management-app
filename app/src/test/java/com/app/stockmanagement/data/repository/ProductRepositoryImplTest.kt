package com.app.stockmanagement.data.repository

import com.app.stockmanagement.data.local.dao.ProductDao
import com.app.stockmanagement.data.local.entity.ProductEntity
import com.app.stockmanagement.data.local.entity.ProductWithSupplierEntity
import com.app.stockmanagement.data.local.entity.SupplierEntity
import com.app.stockmanagement.domain.model.Product
import com.app.stockmanagement.domain.model.ProductWithSupplier
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


class ProductRepositoryImplTest {

    private val productDao: ProductDao = mockk()
    private lateinit var repository: ProductRepositoryImpl
    private lateinit var testProductWithSupplierEntity: ProductWithSupplierEntity

    @Before
    fun setup() {
        repository = ProductRepositoryImpl(productDao)
        testProductWithSupplierEntity = ProductWithSupplierEntity(
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
    }

    @Test
    fun `addProduct calls dao addProduct once with correct parameters`(): Unit = runTest {
        // GIVEN
        val testProduct = Product(
            name = "Twix",
            description = "Tasty chocolate",
            price = 2.0,
            category = "Food",
            barcode = "12321313",
            supplierId = 1,
            currentStockLevel = 11,
            minimumStockLevel = 11
        )
        coEvery { productDao.addProduct(any()) } returns Unit

        // WHEN
        repository.addProduct(testProduct)

        // THEN
        coVerify(exactly = 1) { productDao.addProduct(testProduct) }
    }

    @Test
    fun `updateProduct calls dao updateProduct once with correct parameters`(): Unit = runTest {
        // GIVEN
        val testProduct = ProductWithSupplier(
            name = "Gadget",
            description = "Sample description",
            price = 11.0,
            category = "Food",
            id = 1,
            barcode = "123456789111",
            currentStockLevel = 11,
            minimumStockLevel = 11,
            supplierName = "Global supplier",
        )
        coEvery { productDao.updateProduct(any()) } returns Unit

        // WHEN
        repository.updateProduct(testProduct)

        // THEN
        coVerify(exactly = 1) { productDao.updateProduct(testProduct) }
    }


    @Test
    fun `getProductsWithLowStock calls dao getProductsWithLowStock once with correct parameters and result`(): Unit =
        runTest {
            // GIVEN
            val expectedLength = 1
            val amount = 12
            every { productDao.getProductsWithLowStock(any()) } returns flowOf(
                listOf(
                    testProductWithSupplierEntity
                )
            )

            // WHEN
            val actual = repository.getProductsWithLowStock(amount).first()

            // THEN
            assertEquals(expectedLength, actual.size)
            assertEquals(testProductWithSupplierEntity.product.name, actual[0].product.name)
            assertEquals(
                testProductWithSupplierEntity.supplierEntity.name, actual[0].supplierEntity.name
            )
            verify(exactly = 1) { productDao.getProductsWithLowStock(amount) }

        }

    @Test
    fun `getAllProductsWithSupplier calls dao getAllProductsWithSupplier once and should return result`(): Unit =
        runTest {
            // GIVEN
            val expectedLength = 1
            every { productDao.getAllProductsWithSupplier() } returns flowOf(
                listOf(
                    testProductWithSupplierEntity
                )
            )

            // WHEN
            val actual = repository.getAllProductsWithSupplier().first()

            // THEN
            assertEquals(expectedLength, actual.size)
            assertEquals(testProductWithSupplierEntity.product.name, actual[0].product.name)
            assertEquals(
                testProductWithSupplierEntity.supplierEntity.name, actual[0].supplierEntity.name
            )
            verify(exactly = 1) { productDao.getAllProductsWithSupplier() }
        }

    @Test
    fun `searchForProductsByName calls dao searchForProductsByName once and should return result`(): Unit =
        runTest {
            // GIVEN
            val expectedName = "Twix"
            val expectedLength = 1
            every { productDao.searchForProductsByName(expectedName) } returns flowOf(
                listOf(
                    testProductWithSupplierEntity
                )
            )

            // WHEN
            val actual = repository.searchForProductsByName(expectedName).first()

            // THEN
            assertEquals(expectedLength, actual.size)
            assertEquals(testProductWithSupplierEntity.product.name, actual[0].product.name)
            assertEquals(
                testProductWithSupplierEntity.supplierEntity.name, actual[0].supplierEntity.name
            )
            verify(exactly = 1) { productDao.searchForProductsByName(expectedName) }
        }
}


