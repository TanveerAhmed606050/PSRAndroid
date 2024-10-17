package com.example.psrandroid.ui.screen.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psrandroid.dto.UserCredential
import com.example.psrandroid.repository.AuthRepository
import com.example.psrandroid.response.DealerResponse
import com.example.psrandroid.response.LocationResponse
import com.example.psrandroid.response.LoginResponse
import com.example.psrandroid.storage.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.example.psrandroid.utils.Result
import javax.inject.Inject

@HiltViewModel
class AuthVM @Inject constructor(
    private val authRepository: AuthRepository,
    val userPreferences: UserPreferences
) : ViewModel(){
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf("")
//    fun isAlreadyLogin(): Boolean = userPreferences.getUserPreference()?.name != null
    var loginData by mutableStateOf<LoginResponse?>(null)
    var locationData by mutableStateOf<LocationResponse?>(null)
    var dealersList by mutableStateOf<DealerResponse?>(null)

    fun login(userCredential: UserCredential) = viewModelScope.launch {
        isLoading = true
        val result = authRepository.userLogin(userCredential)
        if (result is Result.Success) {
            isLoading = false
            loginData = result.data
            userPreferences.isFirstLaunch = false
        } else if (result is Result.Failure) {
            isLoading = false
            error = result.exception.message ?: "Failure"
        }
    }

    fun register(userCredential: UserCredential) = viewModelScope.launch {
        isLoading = true
        val result = authRepository.register(userCredential)
        if (result is Result.Success) {
            isLoading = false
            loginData = result.data
        } else if (result is Result.Failure) {
            isLoading = false
            error = result.exception.message ?: "Failure"
        }
    }

    fun getLocation() = viewModelScope.launch {
        isLoading = true
        val result = authRepository.getLocation()
        if (result is Result.Success) {
            isLoading = false
            locationData = result.data
        } else if (result is Result.Failure) {
            isLoading = false
            error = result.exception.message ?: "Failure"
        }
    }

    fun getDealersList() = viewModelScope.launch {
        isLoading = true
        val result = authRepository.getDealerList()
        if (result is Result.Success) {
            isLoading = false
            dealersList = result.data
        } else if (result is Result.Failure) {
            isLoading = false
            error = result.exception.message ?: "Failure"
        }
    }
}