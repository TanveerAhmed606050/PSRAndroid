package com.example.psrandroid.ui.screen.dashboard

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psrandroid.repository.DashboardRepository
import com.example.psrandroid.response.DashboardMetal
import com.example.psrandroid.response.LmeResponse
import com.example.psrandroid.response.MetalData
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
    var error by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var premiumUserData by mutableStateOf<PrimeUser?>(null)
    var subMetalData by mutableStateOf<SearchSubMetal?>(null)
    var mainMetalData by mutableStateOf<DashboardMetal?>(null)
    var lmeMetalData by mutableStateOf<LmeResponse?>(null)
    var suggestMainMetals by mutableStateOf<List<MetalData>?>(null)


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
        Log.d("lsdjag", "Metal: $metalName location: $locationId")
        val result = dashboardRepository.getSearchMetals(locationId, metalName)
        if (result is Result.Success) {
            mainMetalData = result.data
            suggestMainMetals = result.data.data
            Log.d("lsdjag", "result: ${mainMetalData?.data?.size}")
        } else if (result is Result.Failure) {
            error = result.exception.message ?: "Failure"
        }
    }

    fun getLMEMetals() = viewModelScope.launch {
        if (lmeMetalData == null) {
            val result = dashboardRepository.getLMEMetals()
            if (result is Result.Success) {
                lmeMetalData = result.data
            } else if (result is Result.Failure) {
                error = result.exception.message ?: "Failure"
            }
        }
    }

    fun searchMainMetals(searchText: String) {
        suggestMainMetals = mainMetalData?.data?.filter {
            it.name.contains(searchText, ignoreCase = true) ||
                    it.urduName.contains(searchText)
        } ?: listOf()
    }
}