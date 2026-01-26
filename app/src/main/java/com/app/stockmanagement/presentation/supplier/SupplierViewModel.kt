package com.app.stockmanagement.presentation.supplier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stockmanagement.data.mapper.toDomain
import com.app.stockmanagement.domain.repository.SupplierRepository
import com.app.stockmanagement.util.Constants.ERROR
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupplierViewModel @Inject constructor(private val repository: SupplierRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(SupplierUiState())
    val uiState: StateFlow<SupplierUiState> = _uiState
    private val _eventFlow = MutableSharedFlow<SupplierUiEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getAllSuppliers()
    }

    private fun getAllSuppliers() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.getAllSuppliers().catch {
                _eventFlow.emit(SupplierUiEffect.ShowError(ERROR))
            }.collect { suppliers ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    suppliers = suppliers.map { supplier -> supplier.toDomain() })
            }
        }
    }
}