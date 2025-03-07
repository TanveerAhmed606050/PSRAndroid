package com.example.psrandroid.repository

import com.example.psrandroid.network.ApiInterface
import com.example.psrandroid.ui.screen.profile.models.UpdateUserData
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val apiInterface: ApiInterface) :
    BaseRepository() {
    suspend fun updatePassword(updateUserData: UpdateUserData) = loadResource {
        apiInterface.updatePassword(updateUserData)
    }
}