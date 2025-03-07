package com.example.psrandroid.dto

import com.google.gson.annotations.SerializedName

data class UserCredential(
    val phone: String,
    val password: String = "",
    val name: String = "",
    val location: String = "",
    @SerializedName("user_id")
    val userId: String = "",
    @SerializedName("device_id")
    val deviceId: String = ""
)
