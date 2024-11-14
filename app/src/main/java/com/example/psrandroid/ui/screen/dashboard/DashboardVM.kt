package com.example.psrandroid.ui.screen.dashboard

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psrandroid.repository.DashboardRepository
import com.example.psrandroid.response.DashboardMetal
import com.example.psrandroid.response.PrimeUser
import com.example.psrandroid.response.SearchSubMetal
import com.example.psrandroid.storage.UserPreferences
import com.example.psrandroid.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardVM @Inject constructor(
    private val dashboardRepository: DashboardRepository,
    val userPreferences: UserPreferences
) : ViewModel() {
    //    var dashboardData by mutableStateOf<DashboardResponse?>(null)
    var error by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var premiumUserData by mutableStateOf<PrimeUser?>(null)
    var subMetalData by mutableStateOf<SearchSubMetal?>(null)
    var mainMetalData by mutableStateOf<DashboardMetal?>(null)

//    fun getDashboardData(locationId: String, date: String) = viewModelScope.launch {
//        if (dashboardData == null) {
//            isLoading = true
//            val result = dashboardRepository.getDashboardData(locationId, date)
//            if (result is Result.Success) {
//                isLoading = false
//                dashboardData = result.data
//            } else if (result is Result.Failure) {
//                isLoading = false
//                error = result.exception.message ?: "Failure"
//            }
//        }
//    }

    fun getPremiumUser() = viewModelScope.launch {
        if (premiumUserData == null) {
//            isLoading = true
            val result = dashboardRepository.getPremiumUser()
            if (result is Result.Success) {
//                isLoading = false
                premiumUserData = result.data
            } else if (result is Result.Failure) {
//                isLoading = false
                error = result.exception.message ?: "Failure"
            }
        }
    }

    fun getSubMetals(locationId: String, metalName: String) = viewModelScope.launch {
        val result = dashboardRepository.getSubMetals(locationId, metalName)
        if (result is Result.Success) {
            subMetalData = result.data
        } else if (result is Result.Failure) {
            error = result.exception.message ?: "Failure"
        }
    }

    fun getMainMetals(locationId: String, metalName: String) = viewModelScope.launch {
        Log.d("lsdjag", "Metal: $metalName")
        val result = dashboardRepository.getSearchMetals(locationId, metalName)
        if (result is Result.Success) {
            mainMetalData = result.data
            Log.d("lsdjag", "result: ${mainMetalData?.data?.size}")
        } else if (result is Result.Failure) {
            error = result.exception.message ?: "Failure"
        }
    }

}