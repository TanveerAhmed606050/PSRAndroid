package com.example.psrandroid.ui.screen.dashboard

import androidx.lifecycle.ViewModel
import com.example.psrandroid.repository.DashboardRepository
import com.example.psrandroid.storage.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardVM @Inject constructor(
    val dashboardRepository: DashboardRepository,
    val userPreferences: UserPreferences
) : ViewModel() {
}