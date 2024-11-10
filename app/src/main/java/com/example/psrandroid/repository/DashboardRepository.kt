package com.example.psrandroid.repository

import com.example.psrandroid.network.ApiInterface
import javax.inject.Inject

class DashboardRepository @Inject constructor(private val apiInterface: ApiInterface) :
    BaseRepository() {
    suspend fun getDashboardData(locationId: String, date: String) = loadResource {
        apiInterface.getDashboardData(locationId, date)
    }

    suspend fun getPremiumUser() = loadResource {
        apiInterface.getPremiumUser()
    }
    suspend fun getSearchMetals(locationId: String, metalName: String) = loadResource {
        apiInterface.getSearchMetals(locationId, metalName)
    }
    suspend fun getSubMetals(locationId: String, metalName: String) = loadResource {
        apiInterface.getSubMetals(locationId, metalName)
    }
}