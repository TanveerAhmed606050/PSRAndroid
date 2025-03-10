package com.pakscrap.ui.screen.rate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pakscrap.repository.RateRepository
import com.pakscrap.response.LmeData
import com.pakscrap.response.LmeResponse
import com.pakscrap.response.PrimeUser
import com.pakscrap.response.PrimeUserData
import com.pakscrap.storage.UserPreferences
import com.pakscrap.utils.Result
import com.google.android.gms.ads.rewarded.RewardedAd
import com.pakscrap.ui.screen.rate.models.MainMetalData
import com.pakscrap.ui.screen.rate.models.MetalRateResponse
import com.pakscrap.ui.screen.rate.models.RateSuggestionResponse
import com.pakscrap.ui.screen.rate.models.SubMetals
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
    private var premiumUserData by mutableStateOf<PrimeUser?>(null)
    var searchPrimeUserData by mutableStateOf<List<PrimeUserData>?>(null)
    var mainSuggestResponse by mutableStateOf<RateSuggestionResponse?>(null)

    var subMetalResponse by mutableStateOf<MetalRateResponse?>(null)
    var suggestSubMetals by mutableStateOf<List<SubMetals>?>(null)
    var subMetalsList by mutableStateOf<List<SubMetals>?>(null)
    private var lmeMetalData by mutableStateOf<LmeResponse?>(null)
    var suggestMainMetals by mutableStateOf<List<MainMetalData>?>(null)
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
            subMetalResponse = result.data
            if (result.data.data.isNotEmpty()) {
                subMetalsList = result.data.data[0].submetals
                suggestSubMetals = result.data.data[0].submetals
            }
        } else if (result is Result.Failure) {
            error = result.exception.message ?: "Failure"
        }
    }

    fun getMainMetals(locationId: String, metalName: String) = viewModelScope.launch {
        if (mainSuggestResponse == null) {
            isLoading = true
            val result = dashboardRepository.getSearchMetals(locationId, metalName)
            isLoading = false
            if (result is Result.Success) {
                mainSuggestResponse = result.data
                suggestMainMetals = result.data.data
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
        suggestMainMetals = mainSuggestResponse?.data?.filter {
            it.name.contains(searchText, ignoreCase = true) ||
                    it.urduName.contains(searchText)
        } ?: listOf()
    }

    fun searchPrimeUser(search: String) = viewModelScope.launch {
        searchPrimeUserData =
            premiumUserData?.data?.filter { it.name.contains(search, ignoreCase = true) }
    }

    fun searchSubMetals(searchText: String) {
        suggestSubMetals = subMetalsList?.filter {
            it.submetalName.contains(searchText, ignoreCase = true) ||
                    it.submetalUrduName.contains(searchText)
        } ?: listOf()
    }
}