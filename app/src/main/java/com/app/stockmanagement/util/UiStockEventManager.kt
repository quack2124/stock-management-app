package com.app.stockmanagement.util

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UiStockEventManager @Inject constructor() {
    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    suspend fun triggerSnackbar(message: String) {
        _events.emit(message)
    }
}