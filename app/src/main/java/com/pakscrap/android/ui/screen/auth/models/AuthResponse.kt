package com.pakscrap.android.ui.screen.auth.models

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    val data: AuthData,
    val message: String,
    val status: Boolean
)

data class AuthData(
    @SerializedName("created_at")
    val createdAt: String = "",
    val id: Int,
    val location: String,
    val name: String,
    val phone: String,
    @SerializedName("updated_at")
    val updatedAt: String = "",
    @SerializedName("profile_pic")
    val profilePic: String = ""
) {
    companion object
}

val AuthData.Companion.mockup by lazy {
    AuthData(
        createdAt = "",
        id = 2268,
        location = "Lahore",
        name = "User",
        phone = "+923451234567",
        updatedAt = "12-11-25",
        profilePic = ""
    )
}
