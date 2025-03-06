package com.pak.scrap.repository

import com.pak.scrap.network.ApiInterface
import com.pak.scrap.ui.screen.profile.models.UpdateUserData
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val apiInterface: ApiInterface) :
    BaseRepository() {
    suspend fun updatePassword(updateUserData: UpdateUserData) = loadResource {
        apiInterface.updatePassword(updateUserData)
    }
}