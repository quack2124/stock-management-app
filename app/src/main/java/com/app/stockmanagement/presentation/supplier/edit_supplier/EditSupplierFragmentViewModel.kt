package com.app.stockmanagement.presentation.supplier.edit_supplier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stockmanagement.domain.model.Supplier
import com.app.stockmanagement.domain.repository.SupplierRepository
import com.app.stockmanagement.util.Constants.ERROR
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditSupplierFragmentViewModel @Inject constructor(private val repository: SupplierRepository) :
    ViewModel() {

    //    private val _uiState = MutableStateFlow(SupplierUiState())
//    val uiState: StateFlow<SupplierUiState> = _uiState
    private val _eventFlow = MutableSharedFlow<EditSupplierUiEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun updateSupplier(supplier: Supplier) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateSupplier(supplier)
                _eventFlow.emit(EditSupplierUiEffect.Success)
            } catch (er: Exception) {
                _eventFlow.emit(EditSupplierUiEffect.ShowError(ERROR))
            }
        }
    }
}