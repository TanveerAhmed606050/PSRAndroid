package com.pakscrap.repository

import com.pakscrap.network.ApiInterface
import com.pakscrap.ui.screen.profile.models.UpdateUserData
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val apiInterface: ApiInterface) :
    BaseRepository() {
    suspend fun updatePassword(updateUserData: UpdateUserData) = loadResource {
        apiInterface.updatePassword(updateUserData)
    }
}