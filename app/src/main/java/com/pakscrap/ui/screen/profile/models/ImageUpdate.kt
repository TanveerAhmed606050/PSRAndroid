package com.pakscrap.ui.screen.profile.models

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class ImageUpdate(
    @SerializedName("user_id")
    val userId: String,
    val image: MultipartBody.Part,
)
