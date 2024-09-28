package com.example.psrandroid.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val data: LoginData,
    val message: String,
    val status: Boolean
)

data class LoginData(val otp: Int, val user: User)

data class User(
    @SerializedName("created_at")
    val createdAt: String = "",
    val email: String,
    val id: Int = 0,
    val name: String,
    val password: String = "",
    val phone: String = "",
    @SerializedName("profile_pic")
    val profilePic: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
)