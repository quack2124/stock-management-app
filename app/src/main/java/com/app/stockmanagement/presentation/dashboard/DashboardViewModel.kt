package com.app.stockmanagement.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.app.stockmanagement.data.mapper.toDomain
import com.app.stockmanagement.data.worker.StockCheckWorker
import com.app.stockmanagement.domain.repository.ProductRepository
import com.app.stockmanagement.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val productRepository: ProductRepository,
    private val workManager: WorkManager

) : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardFragmentUiState())
    val uiState: StateFlow<DashboardFragmentUiState> = _uiState

    init {
        startWorkerManager()
    }

    fun getTransactionsInLast24Hrs() {
        val currentTime = System.currentTimeMillis()
        val twentyFourHoursAgo = Date(currentTime - (24 * 60 * 60 * 1000))
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            transactionRepository.getAllTransactionsInLastHrs(twentyFourHoursAgo)
                .collect { transactions ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        transactions = transactions.map { it.toDomain() })
                }
        }
    }

    private fun startWorkerManager() {
        workManager.cancelAllWork()
        val stockCheckRequest = PeriodicWorkRequestBuilder<StockCheckWorker>(
            15, TimeUnit.MINUTES
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .build()
        workManager.enqueueUniquePeriodicWork(
            "StockCheckWorkerName",
            ExistingPeriodicWorkPolicy.KEEP,
            stockCheckRequest
        )
    }

    fun getLowStockProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            productRepository.getProductsWithLowStock(2).collect { products ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    products = products.map { it.toDomain() })
            }
        }

    }
}