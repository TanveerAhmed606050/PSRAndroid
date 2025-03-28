package com.pakscrap.repository

import com.pakscrap.dto.ImageUpdate
import com.pakscrap.dto.UserCredential
import com.pakscrap.network.ApiInterface
import com.pakscrap.ui.screen.profile.models.UpdateUserData
import okhttp3.MediaType.Companion.toMediaTypeOrNull
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
            userId = imageUpdate.userId.toRequestBody("text/plain".toMediaTypeOrNull()),
            image = imageUpdate.image
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