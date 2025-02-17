package com.example.psrandroid.ui.screen.profile.models

import com.google.gson.annotations.SerializedName

data class UpdateUserData(
    @SerializedName("user_id")
    val userId: String = "",
    @SerializedName("current_password")
    val currentPassword: String = "",
    @SerializedName("new_password")
    val newPassword: String = "",
    val phone: String = "",
)
