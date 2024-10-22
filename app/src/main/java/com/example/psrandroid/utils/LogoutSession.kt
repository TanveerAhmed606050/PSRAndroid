package com.example.psrandroid.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object LogoutSession {
    private val _errorMessages = MutableStateFlow<String?>(null)
    val errorMessages: StateFlow<String?> = _errorMessages

    fun apiError(message: String){
        _errorMessages.value = message
    }

    fun clearError() {
        _errorMessages.value = null
    }
}