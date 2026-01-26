package com.app.stockmanagement.presentation.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stockmanagement.data.local.entity.Type
import com.app.stockmanagement.data.mapper.toDomain
import com.app.stockmanagement.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState: StateFlow<TransactionUiState> = _uiState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.getAllTransactionsWithProduct().collect { transactions ->
                _uiState.value = _uiState.value.copy(
                    cachedTransactions = transactions.map { it.toDomain() },
                    isLoading = false,
                    transactions = transactions.map { it.toDomain() }

                )
            }
        }
    }

    fun filterResults(selectedType: Type?, viewId: Int) {
        when (selectedType) {
            Type.RESTOCK -> {
                val filtered = _uiState.value.cachedTransactions.filter { it.type == Type.RESTOCK }
                _uiState.value = _uiState.value.copy(
                    transactions = filtered,
                    checkedElement = viewId
                )
            }

            Type.SALE -> {
                val filtered = _uiState.value.cachedTransactions.filter { it.type == Type.SALE }
                _uiState.value = _uiState.value.copy(
                    transactions = filtered,
                    checkedElement = viewId
                )
            }

            null -> {
                val filtered = _uiState.value.cachedTransactions
                _uiState.value = _uiState.value.copy(
                    transactions = filtered,
                    checkedElement = viewId
                )
            }
        }

    }

}