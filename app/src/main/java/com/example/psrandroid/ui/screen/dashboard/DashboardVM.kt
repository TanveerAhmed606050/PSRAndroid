package com.example.psrandroid.ui.screen.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psrandroid.repository.DashboardRepository
import com.example.psrandroid.response.DashboardResponse
import com.example.psrandroid.storage.UserPreferences
import com.example.psrandroid.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardVM @Inject constructor(
    val dashboardRepository: DashboardRepository,
    val userPreferences: UserPreferences
) : ViewModel() {
    var dashboardData by mutableStateOf<DashboardResponse?>(null)
    var error by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    fun getDashboardData(locationId: String, date: String) = viewModelScope.launch {
        isLoading = true
        val result = dashboardRepository.getDashboardData(locationId, date)
        if (result is Result.Success) {
            isLoading = false
            dashboardData = result.data
        } else if (result is Result.Failure) {
            isLoading = false
            error = result.exception.message ?: "Failure"
        }
    }

}