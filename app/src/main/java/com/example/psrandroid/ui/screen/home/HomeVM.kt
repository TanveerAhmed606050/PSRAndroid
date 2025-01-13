package com.example.psrandroid.ui.screen.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psrandroid.dto.UserCredential
import com.example.psrandroid.repository.AuthRepository
import com.example.psrandroid.response.AuthResponse
import com.example.psrandroid.storage.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(
    private val authRepository: AuthRepository,
    val userPreferences: UserPreferences
) : ViewModel() {
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf("")
    var homeData by mutableStateOf<AuthResponse?>(null)
    fun login(userCredential: UserCredential) = viewModelScope.launch {
    }

}