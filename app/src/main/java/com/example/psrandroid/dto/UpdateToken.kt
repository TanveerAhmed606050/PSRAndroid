package com.example.psrandroid.dto

import com.google.gson.annotations.SerializedName

data class UpdateToken(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("device_id")
    val deviceToken: String
)