package com.pak.scrap.ui.screen.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pak.scrap.repository.ProfileRepository
import com.pak.scrap.storage.UserPreferences
import com.pak.scrap.ui.screen.auth.models.InfoDataResponse
import com.pak.scrap.ui.screen.profile.models.UpdateUserData
import com.pak.scrap.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileVM @Inject constructor(
    private val profileRepository: ProfileRepository,
    val userPreferences: UserPreferences
) : ViewModel() {
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf("")
    var infoResponse by mutableStateOf<InfoDataResponse?>(null)

    fun updatePassword(updateUserData: UpdateUserData) = viewModelScope.launch {
        isLoading = true
        val result = profileRepository.updatePassword(updateUserData)
        isLoading = false
        if (result is Result.Success) {
            infoResponse = result.data
        } else if (result is Result.Failure) {
            error = result.exception.message ?: "Failure"
        }
    }
}