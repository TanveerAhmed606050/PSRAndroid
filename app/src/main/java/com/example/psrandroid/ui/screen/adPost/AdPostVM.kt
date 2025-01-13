package com.example.psrandroid.ui.screen.adPost

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psrandroid.repository.HomeRepository
import com.example.psrandroid.storage.UserPreferences
import com.example.psrandroid.ui.screen.adPost.models.AllAds
import com.example.psrandroid.ui.screen.rate.models.AllSubMetalData
import com.example.psrandroid.ui.screen.rate.models.SubData
import com.example.psrandroid.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdPostVM @Inject constructor(
    private val homeRepository: HomeRepository,
    val userPreferences: UserPreferences
) : ViewModel() {
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf("")
    var adsData by mutableStateOf<AllAds?>(null)
    var locationAds by mutableStateOf<AllAds?>(null)
    var subMetalData by mutableStateOf<AllSubMetalData?>(null)
    var suggestSubMetals by mutableStateOf<List<SubData>?>(null)

    fun getAllAds() = viewModelScope.launch {
        if (adsData == null) {
            isLoading = true
            val result = homeRepository.getAllAds()
            isLoading = false
            if (result is Result.Success) {
                adsData = result.data
            } else if (result is Result.Failure) {
                error = result.exception.message ?: "Failure"
            }
        }
    }

    fun getAdsByLocation(location: String) = viewModelScope.launch {
        isLoading = true
        val result = homeRepository.getAdsByLocation(location)
        isLoading = false
        if (result is Result.Success) {
            adsData = result.data
        } else if (result is Result.Failure) {
            error = result.exception.message ?: "Failure"
        }
    }

    fun getAllSubMetals() = viewModelScope.launch {
        if (subMetalData == null) {
            val result = homeRepository.getAllSubMetals()
            if (result is Result.Success) {
                subMetalData = result.data
            } else if (result is Result.Failure) {
                error = result.exception.message ?: "Failure"
            }
        }
    }

    fun searchSubMetals(searchText: String) {
        suggestSubMetals = subMetalData?.data?.filter {
            it.name.contains(searchText, ignoreCase = true) ||
                    it.urduName.contains(searchText)
        } ?: listOf()
    }

}