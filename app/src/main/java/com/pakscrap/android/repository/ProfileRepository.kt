package com.pakscrap.android.repository

import com.pakscrap.android.network.ApiInterface
import com.pakscrap.android.ui.screen.profile.models.UpdateUserData
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val apiInterface: ApiInterface) :
    BaseRepository() {
    suspend fun updatePassword(updateUserData: UpdateUserData) = loadResource {
        apiInterface.updatePassword(updateUserData)
    }
}