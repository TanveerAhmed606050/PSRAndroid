package com.pakscrap.dto

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class ImageUpdate(
    @SerializedName("user_id")
    val userId: String,
    val image: MultipartBody.Part,
)
