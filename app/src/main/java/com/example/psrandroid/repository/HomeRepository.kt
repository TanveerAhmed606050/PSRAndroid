package com.example.psrandroid.repository

import com.example.psrandroid.network.ApiInterface
import javax.inject.Inject

class HomeRepository @Inject constructor(private val apiInterface: ApiInterface) :
    BaseRepository() {
    suspend fun getAllAds() = loadResource {
        apiInterface.getAllAds()
    }

}