package com.example.psrandroid.ui.screen.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psrandroid.repository.HomeRepository
import com.example.psrandroid.storage.UserPreferences
import com.example.psrandroid.ui.screen.home.model.HomeResponse
import com.example.psrandroid.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(
    private val homeRepository: HomeRepository,
    val userPreferences: UserPreferences
) : ViewModel() {
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf("")
    var homeResponse by mutableStateOf<HomeResponse?>(null)
    fun getHomeData(userId: String) = viewModelScope.launch {
        isLoading = true
        val result = homeRepository.getHomeData(userId)
        isLoading = false
        if (result is Result.Success) {
            homeResponse = result.data
        } else if (result is Result.Failure) {
            error = result.exception.message ?: "Failure"
        }
    }

}