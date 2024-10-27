package com.example.psrandroid.ui.screen.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psrandroid.dto.ImageUpdate
import com.example.psrandroid.dto.UpdateLocation
import com.example.psrandroid.dto.UserCredential
import com.example.psrandroid.repository.AuthRepository
import com.example.psrandroid.response.AuthResponse
import com.example.psrandroid.response.DealerResponse
import com.example.psrandroid.response.LocationResponse
import com.example.psrandroid.response.User
import com.example.psrandroid.response.mockup
import com.example.psrandroid.storage.UserPreferences
import com.example.psrandroid.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthVM @Inject constructor(
    private val authRepository: AuthRepository,
    val userPreferences: UserPreferences
) : ViewModel() {
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf("")

    //    fun isAlreadyLogin(): Boolean = userPreferences.getUserPreference()?.name != null
    var loginData by mutableStateOf<AuthResponse?>(null)
    var locationData by mutableStateOf<LocationResponse?>(null)
    var dealersList by mutableStateOf<DealerResponse?>(null)

    fun login(userCredential: UserCredential) = viewModelScope.launch {
        isLoading = true
        val result = authRepository.userLogin(userCredential)
        if (result is Result.Success) {
            isLoading = false
            loginData = result.data
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
//        isLoading = true
        val result = authRepository.getLocation()
        if (result is Result.Success) {
//            isLoading = false
            locationData = result.data
            if (locationData != null)
                userPreferences.saveLocationList(locationData ?: LocationResponse.mockup)
        } else if (result is Result.Failure) {
//            isLoading = false
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

    fun updateUserLocation(updateLocation: UpdateLocation) = viewModelScope.launch {
        isLoading = true
        val result = authRepository.updateUserLocation(updateLocation)
        if (result is Result.Success) {
            isLoading = false
            loginData = result.data
            userPreferences.saveUserPreference(
                User(
                    userId = loginData?.data?.id ?: 0,
                    name = loginData?.data?.name ?: "",
                    phone = loginData?.data?.phone ?: "",
                    location = loginData?.data?.location ?: "",
                    profilePic = loginData?.data?.name ?: ""
                )
            )
        } else if (result is Result.Failure) {
            isLoading = false
            error = result.exception.message ?: "Failure"
        }
    }

    fun updateUserImage(imageUpdate: ImageUpdate) = viewModelScope.launch {
        isLoading = true
        val result = authRepository.updateUserImage(imageUpdate)
        if (result is Result.Success) {
            isLoading = false
            loginData = result.data
            userPreferences.saveUserPreference(
                User(
                    userId = loginData?.data?.id ?: 0,
                    name = loginData?.data?.name ?: "",
                    phone = loginData?.data?.phone ?: "",
                    location = loginData?.data?.location ?: "",
                    profilePic = loginData?.data?.profilePic ?: ""
                )
            )
        } else if (result is Result.Failure) {
            isLoading = false
            error = result.exception.message ?: "Failure"
        }
    }

    fun updateUserData(userCredential: UserCredential) = viewModelScope.launch {
        isLoading = true
        val result = authRepository.updateUserData(userCredential)
        if (result is Result.Success) {
            isLoading = false
            loginData = result.data
//            userPreferences.saveUserPreference(
//                User(
//                    userId = loginData?.data?.id ?: 0,
//                    name = loginData?.data?.name ?: "",
//                    phone = loginData?.data?.phone ?: "",
//                    location = loginData?.data?.location ?: "",
//                    profilePic = loginData?.data?.profilePic?:""
//                )
//            )
        } else if (result is Result.Failure) {
            isLoading = false
            error = result.exception.message ?: "Failure"
        }
    }

}