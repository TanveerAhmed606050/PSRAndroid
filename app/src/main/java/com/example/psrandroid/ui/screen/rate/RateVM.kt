package com.example.psrandroid.ui.screen.rate

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psrandroid.repository.RateRepository
import com.example.psrandroid.response.LmeData
import com.example.psrandroid.response.LmeResponse
import com.example.psrandroid.ui.screen.rate.models.MetalData
import com.example.psrandroid.response.PrimeUser
import com.example.psrandroid.response.PrimeUserData
import com.example.psrandroid.ui.screen.rate.models.RateMetal
import com.example.psrandroid.response.SearchSubMetal
import com.example.psrandroid.response.SubMetalData
import com.example.psrandroid.storage.UserPreferences
import com.example.psrandroid.utils.Result
import com.google.android.gms.ads.rewarded.RewardedAd
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RateVM @Inject constructor(
    private val dashboardRepository: RateRepository,
    val userPreferences: UserPreferences
) : ViewModel() {
    var error by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var premiumUserData by mutableStateOf<PrimeUser?>(null)
    var searchPrimeUserData by mutableStateOf<List<PrimeUserData>?>(null)
    var subMetalData by mutableStateOf<SearchSubMetal?>(null)
    var mainMetalData by mutableStateOf<RateMetal?>(null)
    var lmeMetalData by mutableStateOf<LmeResponse?>(null)
    var suggestMainMetals by mutableStateOf<List<MetalData>?>(null)
    private var suggestSubMetals by mutableStateOf<List<SubMetalData>?>(null)
    var searchLmeMetalData by mutableStateOf<List<LmeData>?>(null)
    var watchAd by mutableStateOf(false)
    var rewardedAd by mutableStateOf<RewardedAd?>(null)

    fun getPremiumUser() = viewModelScope.launch {
        if (premiumUserData == null) {
            val result = dashboardRepository.getPremiumUser()
            if (result is Result.Success) {
                premiumUserData = result.data
                searchPrimeUserData = result.data.data
            } else if (result is Result.Failure) {
                error = result.exception.message ?: "Failure"
            }
        }
    }

    fun getSubMetals(locationId: String, metalName: String) = viewModelScope.launch {
        isLoading = true
        val result = dashboardRepository.getSubMetals(locationId, metalName)
        isLoading = false
        if (result is Result.Success) {
            subMetalData = result.data
            suggestSubMetals = result.data.data
        } else if (result is Result.Failure) {
            error = result.exception.message ?: "Failure"
        }
    }

    fun getMainMetals(locationId: String, metalName: String) = viewModelScope.launch {
        if (mainMetalData == null) {
            isLoading = true
            val result = dashboardRepository.getSearchMetals(locationId, metalName)
            isLoading = false
            if (result is Result.Success) {
                mainMetalData = result.data
                suggestMainMetals = result.data.data
                Log.d("lsdjag", "result: ${mainMetalData?.data?.size}")
            } else if (result is Result.Failure) {
                error = result.exception.message ?: "Failure"
            }
        }
    }

    fun getLMEMetals() = viewModelScope.launch {
        if (lmeMetalData == null) {
            val result = dashboardRepository.getLMEMetals()
            if (result is Result.Success) {
                lmeMetalData = result.data
                searchLmeMetalData = lmeMetalData?.data
            } else if (result is Result.Failure) {
                error = result.exception.message ?: "Failure"
            }
        }
    }

    fun searchLME(search: String) = viewModelScope.launch {
        searchLmeMetalData =
            lmeMetalData?.data?.filter { it.name.contains(search, ignoreCase = true) }
    }

    fun searchMainMetals(searchText: String) {
        suggestMainMetals = mainMetalData?.data?.filter {
            it.name.contains(searchText, ignoreCase = true) ||
                    it.urduName.contains(searchText)
        } ?: listOf()
    }

    fun searchPrimeUser(search: String) = viewModelScope.launch {
        searchPrimeUserData =
            premiumUserData?.data?.filter { it.name.contains(search, ignoreCase = true) }
    }

}