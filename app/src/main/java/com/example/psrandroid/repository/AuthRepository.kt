package com.example.psrandroid.repository

import com.example.psrandroid.dto.ImageUpdate
import com.example.psrandroid.dto.UserCredential
import com.example.psrandroid.network.ApiInterface
import com.example.psrandroid.ui.screen.profile.models.UpdateUserData
import okhttp3.RequestBody.Companion.toRequestBody
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

    suspend fun updateUserImage(imageUpdate: ImageUpdate) = loadResource {
        apiInterface.updateUserImage(
            userId = imageUpdate.userId.toRequestBody(),
            imageUpdate = imageUpdate.image
        )
    }

    suspend fun resetPassword(updateUserData: UpdateUserData) = loadResource {
        apiInterface.resetPassword(updateUserData)
    }

    suspend fun phoneValidate(updateUserData: UpdateUserData) = loadResource {
        apiInterface.phoneValidate(updateUserData)
    }

    suspend fun updateUserData(userCredential: UserCredential) = loadResource {
        apiInterface.updateUserData(userCredential)
    }
}