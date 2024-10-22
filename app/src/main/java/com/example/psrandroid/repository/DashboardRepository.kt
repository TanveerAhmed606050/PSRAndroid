package com.example.psrandroid.repository

import com.example.psrandroid.network.ApiInterface
import javax.inject.Inject

class DashboardRepository @Inject constructor(private val apiInterface: ApiInterface) :
    BaseRepository() {
    suspend fun getDashboardData(locationId: String, date: String) = loadResource {
        apiInterface.getDashboardData(locationId, date)
    }

}