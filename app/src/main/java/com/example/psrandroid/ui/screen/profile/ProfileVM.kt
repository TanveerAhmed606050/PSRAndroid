package com.example.psrandroid.ui.screen.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.psrandroid.repository.ProfileRepository
import com.example.psrandroid.storage.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileVM @Inject constructor(
    val profileRepository: ProfileRepository,
    val userPreferences: UserPreferences
) : ViewModel() {
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf("")

}