package com.pakscrap.repository

import com.pakscrap.network.ApiInterface
import javax.inject.Inject

class RateRepository @Inject constructor(private val apiInterface: ApiInterface) :
    BaseRepository() {
    suspend fun getPremiumUser() = loadResource {
        apiInterface.getPremiumUser()
    }

    suspend fun getSearchMetals(locationId: String, metalName: String) = loadResource {
        apiInterface.getSearchMetals(locationId, metalName)
    }

    suspend fun getSubMetals(locationId: String, metalName: String) = loadResource {
        apiInterface.getSubMetals(locationId, metalName)
    }

    suspend fun getLMEMetals() = loadResource {
        apiInterface.getLMEMetals()
    }
}