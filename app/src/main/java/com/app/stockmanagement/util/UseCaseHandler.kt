package com.app.stockmanagement.util

interface UseCaseHandler<T> {
    fun onSuccess(result: T)
    fun onFailure()
}