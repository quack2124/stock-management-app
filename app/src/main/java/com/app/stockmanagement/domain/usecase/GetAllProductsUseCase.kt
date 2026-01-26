package com.app.stockmanagement.domain.usecase

import com.app.stockmanagement.data.local.entity.ProductWithSupplierEntity
import com.app.stockmanagement.domain.repository.ProductRepository
import com.app.stockmanagement.util.UseCaseHandler
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class GetAllProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {

    suspend operator fun invoke(callback: UseCaseHandler<List<ProductWithSupplierEntity>>) {
        productRepository.getAllProductsWithSupplier().catch {
            callback.onFailure()
        }.collect {
            callback.onSuccess(it)
        }
    }

}

