package com.example.psrandroid.repository

import com.example.psrandroid.network.ApiInterface
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class HomeRepository @Inject constructor(private val apiInterface: ApiInterface) :
    BaseRepository() {

    suspend fun getAdsByLocation(location: String) = loadResource {
        apiInterface.getAdsByLocation(location)
    }

//    suspend fun getAdsByUser(userId: String) = loadResource {
//        apiInterface.getAdsByUser(userId)
//    }

    suspend fun getAllSubMetals() = loadResource {
        apiInterface.getAllSubMetals()
    }

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