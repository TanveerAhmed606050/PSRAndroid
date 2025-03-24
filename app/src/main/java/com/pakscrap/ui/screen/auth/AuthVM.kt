package com.pakscrap.ui.screen.auth

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pakscrap.dto.ImageUpdate
import com.pakscrap.dto.UserCredential
import com.pakscrap.repository.AuthRepository
import com.pakscrap.response.LocationResponse
import com.pakscrap.response.mockup
import com.pakscrap.storage.UserPreferences
import com.pakscrap.ui.screen.auth.models.AuthData
import com.pakscrap.ui.screen.auth.models.AuthResponse
import com.pakscrap.ui.screen.auth.models.InfoDataResponse
import com.pakscrap.ui.screen.auth.models.mockup
import com.pakscrap.ui.screen.profile.models.UpdateUserData
import com.pakscrap.utils.Result
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AuthVM @Inject constructor(
    private val authRepository: AuthRepository,
    val userPreferences: UserPreferences
) : ViewModel() {
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf("")
    private val _verificationId = MutableStateFlow<String?>(null)

    private val _message = MutableStateFlow("")
    val message: StateFlow<String?> = _message

    var loginData by mutableStateOf<AuthResponse?>(null)
    private var locationData by mutableStateOf<LocationResponse?>(null)

    var resetPasswordResponse by mutableStateOf<InfoDataResponse?>(null)

    fun sendVerificationCode(phoneNumber: String, activity: Activity) {
//        isLoading = true
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    _message.value = "Verification completed"
                    signInWithCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    isLoading = false
                    _message.value = "Verification failed: ${e.message}"
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    isLoading = false
                    _verificationId.value = verificationId
                    _message.value = "Code sent to $phoneNumber"
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyCode(code: String) {
        isLoading = true
        val credential = _verificationId.value?.let {
            PhoneAuthProvider.getCredential(it, code)
        }
        credential?.let {
            signInWithCredential(it)
        }
    }

    fun updateMessage(text: String) {
        _message.value = text
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    _message.value = "Verification successful"
                } else {
                    _message.value = "Verification failed: ${task.exception?.message}"
                }
            }
    }

    fun login(userCredential: UserCredential) = viewModelScope.launch {
        isLoading = true
        val result = authRepository.userLogin(userCredential)
        isLoading = false
        if (result is Result.Success) {
            loginData = result.data
            userPreferences.saveUserPreference(
                loginData?.data ?: AuthData.mockup
            )
        } else if (result is Result.Failure) {
            error = result.exception.message ?: "Failure"
        }
    }

    fun register(userCredential: UserCredential) = viewModelScope.launch {
        isLoading = true
        val result = authRepository.register(userCredential)
        isLoading = false
        if (result is Result.Success) {
            loginData = result.data
            userPreferences.saveUserPreference(
                loginData?.data ?: AuthData.mockup
            )
        } else if (result is Result.Failure) {
            error = result.exception.message ?: "Failure"
        }
    }

    fun getLocation() = viewModelScope.launch {
        val result = authRepository.getLocation()
        if (result is Result.Success) {
            locationData = result.data
            if (locationData != null)
                userPreferences.saveLocationList(locationData ?: LocationResponse.mockup)
        } else if (result is Result.Failure) {
            error = result.exception.message ?: "Failure"
        }
    }

    fun updateUserImage(imageUpdate: ImageUpdate) = viewModelScope.launch {
        isLoading = true
//        Log.d("lsjag", "api: $imageUpdate")
        val result = authRepository.updateUserImage(imageUpdate)
//        Log.d("lsjag", "result: $result")
        isLoading = false
        if (result is Result.Success) {
            loginData = result.data
            userPreferences.saveUserPreference(
                loginData?.data ?: AuthData.mockup
            )
        } else if (result is Result.Failure) {
            error = result.exception.message ?: "Failure"
        }
    }

    fun resetPassword(updateUserData: UpdateUserData) = viewModelScope.launch {
        isLoading = true
        val result = authRepository.resetPassword(updateUserData)
        isLoading = false
        if (result is Result.Success) {
            resetPasswordResponse = result.data
        } else if (result is Result.Failure) {
            error = result.exception.message ?: "Failure"
        }
    }

    fun phoneValidate(updateUserData: UpdateUserData) = viewModelScope.launch {
        isLoading = true
        val result = authRepository.phoneValidate(updateUserData)
        isLoading = false
        if (result is Result.Success) {
            resetPasswordResponse = result.data
        } else if (result is Result.Failure) {
            error = result.exception.message ?: "Failure"
        }
    }

    fun updateUserData(userCredential: UserCredential) = viewModelScope.launch {
        isLoading = true
        val result = authRepository.updateUserData(userCredential)
        isLoading = false
        if (result is Result.Success) {
            loginData = result.data
            userPreferences.saveUserPreference(
                loginData?.data ?: AuthData.mockup
            )
        } else if (result is Result.Failure) {
            error = result.exception.message ?: "Failure"
        }
    }

}