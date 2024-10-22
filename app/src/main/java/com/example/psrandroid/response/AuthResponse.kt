package com.example.psrandroid.response

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    val data: AuthData,
    val message: String,
    val status: Boolean
)

data class AuthData(
    @SerializedName("created_at")
    val createdAt: String,
    val id: Int,
    val location: String,
    val name: String,
    val phone: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("profile_pic")
    val profilePic: String
)

data class User(
    val userId: Int, val name: String, val phone: String, val location: String = "",
    @SerializedName("profile_pic")
    val profilePic: String = ""
)