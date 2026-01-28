package com.app.stockmanagement.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.app.stockmanagement.R
import com.app.stockmanagement.domain.repository.ProductRepository
import com.app.stockmanagement.util.UiStockEventManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class StockCheckWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val productRepository: ProductRepository,
    private val uiEventManager: UiStockEventManager
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            val lowStockProducts = productRepository.getProductsWithLowStock(2).first()

            if (lowStockProducts.isNotEmpty()) {
                val msg =
                    if (lowStockProducts.size == 1) context.getString(
                        R.string.please_check_stock_singular,
                        lowStockProducts.size
                    )
                    else context.getString(
                        R.string.please_check_stock_plural,
                        lowStockProducts.size
                    )
                uiEventManager.triggerSnackbar(msg)
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}