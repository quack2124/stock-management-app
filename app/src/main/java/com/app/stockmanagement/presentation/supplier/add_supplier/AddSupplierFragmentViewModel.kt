package com.app.stockmanagement.presentation.supplier.add_supplier

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
class AddSupplierFragmentViewModel @Inject constructor(private val repository: SupplierRepository) :
    ViewModel() {

    private val _eventFlow = MutableSharedFlow<AddSupplierUiEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun addSupplier(supplier: Supplier) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.addSupplier(supplier)
                _eventFlow.emit(AddSupplierUiEffect.Success)
            } catch (er: Exception) {
                _eventFlow.emit(AddSupplierUiEffect.ShowError(ERROR))
            }
        }
    }
}