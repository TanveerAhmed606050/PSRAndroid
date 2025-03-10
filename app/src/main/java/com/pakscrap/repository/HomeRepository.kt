package com.pakscrap.repository

import com.pakscrap.network.ApiInterface
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class HomeRepository @Inject constructor(private val apiInterface: ApiInterface) :
    BaseRepository() {

    suspend fun createPost(
        userId: RequestBody,
        metalName: RequestBody,
        phoneNumber: RequestBody,
        submetal: RequestBody,
        city: RequestBody,
        name: RequestBody,
        description: RequestBody,
        price: RequestBody,
        photos: List<MultipartBody.Part>
    ) = loadResource {
        apiInterface.createPost(
            userId = userId,
            metalName = metalName,
            phoneNumber = phoneNumber,
            submetal = submetal,
            city = city,
            name = name,
            description = description,
            price = price,
            photos = photos
        )
    }

    suspend fun getHomeData() = loadResource {
        apiInterface.getHomeData()
    }
}