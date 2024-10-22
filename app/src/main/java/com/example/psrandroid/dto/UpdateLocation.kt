package com.example.psrandroid.dto

import com.google.gson.annotations.SerializedName

data class UpdateLocation(
    @SerializedName("user_id")
    val userId: Int, val location: String
)
