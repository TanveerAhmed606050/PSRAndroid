package com.example.psrandroid.repository

import com.example.psrandroid.network.ApiInterface
import com.example.psrandroid.ui.screen.adPost.models.CreatePost
import javax.inject.Inject

class HomeRepository @Inject constructor(private val apiInterface: ApiInterface) :
    BaseRepository() {
    suspend fun getAllAds() = loadResource {
        apiInterface.getAllAds()
    }

    suspend fun getAdsByLocation(location: String) = loadResource {
        apiInterface.getAdsByLocation(location)
    }

    suspend fun getAdsByUser(userId: String) = loadResource {
        apiInterface.getAdsByUser(userId)
    }

    suspend fun getAllSubMetals() = loadResource {
        apiInterface.getAllSubMetals()
    }

    suspend fun createPost(createPost: CreatePost) = loadResource {
        apiInterface.createPost(createPost)
    }

    suspend fun getHomeData() = loadResource {
        apiInterface.getHomeData()
    }
}