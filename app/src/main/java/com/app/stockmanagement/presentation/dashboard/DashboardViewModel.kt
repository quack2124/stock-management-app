package com.app.stockmanagement.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stockmanagement.data.mapper.toDomain
import com.app.stockmanagement.domain.repository.ProductRepository
import com.app.stockmanagement.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val productRepository: ProductRepository

) : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardFragmentUiState())
    val uiState: StateFlow<DashboardFragmentUiState> = _uiState


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