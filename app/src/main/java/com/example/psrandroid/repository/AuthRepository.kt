package com.example.psrandroid.repository

import com.example.psrandroid.dto.ImageUpdate
import com.example.psrandroid.dto.UpdateLocation
import com.example.psrandroid.dto.UpdateToken
import com.example.psrandroid.dto.UserCredential
import com.example.psrandroid.network.ApiInterface
import com.example.psrandroid.storage.UserPreferences
import com.example.psrandroid.ui.screen.profile.models.UpdateUserData
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiInterface: ApiInterface,
    private val userPreference: UserPreferences,
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

    suspend fun resetPassword(updateUserData: UpdateUserData) = loadResource {
        apiInterface.resetPassword(updateUserData)
    }

    suspend fun phoneValidate(updateUserData: UpdateUserData) = loadResource {
        apiInterface.phoneValidate(updateUserData)
    }

    suspend fun updateUserData(userCredential: UserCredential) = loadResource {
        apiInterface.updateUserData(userCredential)
    }

    suspend fun updateToken(token: String) {
        if ((userPreference.getUserPreference()?.id ?: 0) >= 1) {
            apiInterface.updateDeviceToken(
                UpdateToken(
                    userId = userPreference.getUserPreference()?.id ?: 0, deviceToken = token
                )
            )
        }
    }
}