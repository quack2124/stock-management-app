package com.app.stockmanagement.domain.usecase

import com.app.stockmanagement.data.local.entity.Type
import com.app.stockmanagement.domain.model.ProductWithSupplier
import com.app.stockmanagement.domain.model.TransactionWithProduct
import com.app.stockmanagement.domain.repository.ProductRepository
import com.app.stockmanagement.domain.repository.TransactionRepository
import com.app.stockmanagement.util.UseCaseHandler
import javax.inject.Inject

class AddTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val productRepository: ProductRepository
) {

    suspend operator fun invoke(
        transaction: TransactionWithProduct,
        productToUpdate: ProductWithSupplier,
        callback: UseCaseHandler<Unit>
    ) {
        val updatedProduct = if (transaction.type == Type.SALE) {
            productToUpdate.copy(currentStockLevel = productToUpdate.currentStockLevel - transaction.quantity)
        } else {
            productToUpdate.copy(currentStockLevel = productToUpdate.currentStockLevel + transaction.quantity)

        }
        try {
            transactionRepository.addTransaction(transaction)
            productRepository.updateProduct(updatedProduct)
            callback.onSuccess(Unit)

        } catch (e: Exception) {
            callback.onFailure()

        }
    }

}