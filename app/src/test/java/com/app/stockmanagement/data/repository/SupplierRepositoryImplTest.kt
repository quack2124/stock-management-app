package com.app.stockmanagement.data.repository

import com.app.stockmanagement.data.local.dao.SupplierDao
import com.app.stockmanagement.data.local.entity.SupplierEntity
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

class SupplierRepositoryImplTest {
    private val supplierDao: SupplierDao = mockk()
    private lateinit var repository: SupplierRepositoryImpl
    private lateinit var mockSupplierEntitiy: SupplierEntity

    @Before
    fun setup() {
        repository = SupplierRepositoryImpl(supplierDao)
        mockSupplierEntitiy = SupplierEntity(
            id = 1,
            name = "Global supplier",
            contactPerson = "Jane Peterson",
            phone = "32131321",
            email = "jane@test.com",
            address = "South street 1"
        )
    }

    @Test
    fun `getAllSuppliers calls dao getAllSuppliers once and should return correct result`(): Unit =
        runTest {
            // GIVEN
            every { supplierDao.getAllSuppliers() } returns flowOf(listOf(mockSupplierEntitiy))

            // WHEN
            val result = repository.getAllSuppliers().first()

            // THEN
            assertEquals(mockSupplierEntitiy.id, result[0].id)
            assertEquals(
                mockSupplierEntitiy.name, result[0].name
            )
            verify(exactly = 1) { supplierDao.getAllSuppliers() }
        }

    @Test
    fun `searchForSupplierByName calls dao searchForSupplierByName once with params and should return correct result`(): Unit =
        runTest {
            // GIVEN
            val mockedSupplierName = "Global supplier"
            every { supplierDao.searchForSupplierByName(mockedSupplierName) } returns flowOf(
                listOf(
                    mockSupplierEntitiy
                )
            )

            // WHEN
            val result = repository.searchForSupplierByName(mockedSupplierName).first()

            // THEN
            assertEquals(mockSupplierEntitiy.id, result[0].id)
            assertEquals(
                mockSupplierEntitiy.name, result[0].name
            )
            verify(exactly = 1) { supplierDao.searchForSupplierByName(mockedSupplierName) }
        }

    @Test
    fun `updateSupplier calls dao updateSupplier once with correct params`(): Unit = runTest {
        // GIVEN
        val mockedSupplier = mockSupplierEntitiy.toDomain()
        coEvery { supplierDao.updateSupplier(mockedSupplier) } returns Unit

        // WHEN
        repository.updateSupplier(mockedSupplier)

        // THEN
        coVerify(exactly = 1) { supplierDao.updateSupplier(mockedSupplier) }
    }

    @Test
    fun `addSupplier calls dao addSupplier once with correct params`(): Unit = runTest {
        // GIVEN
        val mockedSupplier = mockSupplierEntitiy.toDomain()
        coEvery { supplierDao.addSupplier(mockedSupplier) } returns Unit

        // WHEN
        repository.addSupplier(mockedSupplier)

        // THEN
        coVerify(exactly = 1) { supplierDao.addSupplier(mockedSupplier) }
    }
}
