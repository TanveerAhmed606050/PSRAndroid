package com.example.psrandroid.repository

import com.example.psrandroid.dto.ImageUpdate
import com.example.psrandroid.dto.UserCredential
import com.example.psrandroid.dto.UpdateLocation
import com.example.psrandroid.network.ApiInterface
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiInterface: ApiInterface
) :
    BaseRepository() {
    suspend fun userLogin(userCredential: UserCredential) = loadResource {
        apiInterface.userLogin(userCredential)
    }

    suspend fun register(userCredential: UserCredential) = loadResource {
        apiInterface.register(userCredential)
    }
    suspend fun getLocation() = loadResource {
        apiInterface.getLocation()
    }
    suspend fun getDealerList() = loadResource {
        apiInterface.getDealerList()
    }
    suspend fun updateUserLocation(updateLocation: UpdateLocation) = loadResource {
        apiInterface.updateUserLocation(updateLocation)
    }

    suspend fun updateUserImage(imageUpdate: ImageUpdate) = loadResource {
        apiInterface.updateUserImage(imageUpdate)
    }
    suspend fun updateUserData(userCredential: UserCredential) = loadResource {
        apiInterface.updateUserData(userCredential)
    }
}