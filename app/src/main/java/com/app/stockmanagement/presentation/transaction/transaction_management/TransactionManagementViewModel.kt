package com.app.stockmanagement.presentation.transaction.transaction_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stockmanagement.data.local.entity.ProductWithSupplierEntity
import com.app.stockmanagement.data.mapper.toDomain
import com.app.stockmanagement.domain.model.ProductWithSupplier
import com.app.stockmanagement.domain.model.TransactionWithProduct
import com.app.stockmanagement.domain.usecase.AddTransactionsUseCase
import com.app.stockmanagement.domain.usecase.GetAllProductsUseCase
import com.app.stockmanagement.util.Constants.ERROR
import com.app.stockmanagement.util.UseCaseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionManagementViewModel @Inject constructor(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val addTransactionsUseCase: AddTransactionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionManagementUiState())
    val uiState: StateFlow<TransactionManagementUiState> = _uiState

    private val _eventFlow = MutableSharedFlow<TransactionManagementUiEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getAllProductsUseCase(object : UseCaseHandler<List<ProductWithSupplierEntity>> {
                override fun onSuccess(result: List<ProductWithSupplierEntity>) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        products = result.map { it.toDomain() })
                }

                override fun onFailure() {
                    viewModelScope.launch {
                        _eventFlow.emit(TransactionManagementUiEffect.ShowError(ERROR))
                    }
                }
            })
        }
    }

    fun addTransaction(transaction: TransactionWithProduct, productToUpdate: ProductWithSupplier) {
        viewModelScope.launch(Dispatchers.IO) {
            addTransactionsUseCase(transaction, productToUpdate, object : UseCaseHandler<Unit> {
                override fun onSuccess(result: Unit) {
                    viewModelScope.launch {
                        _eventFlow.emit(TransactionManagementUiEffect.Success)
                    }
                }

                override fun onFailure() {
                    viewModelScope.launch {
                        _eventFlow.emit(TransactionManagementUiEffect.ShowError(ERROR))
                    }
                }

            })
        }
    }


}