package com.example.psrandroid.dto

import com.google.gson.annotations.SerializedName

data class AdPostDto(
    @SerializedName("user_id")
    val userId: String = "",
    @SerializedName("per_page")
    val perPage: String = "",
    val page: String = "",
    val city: String = "",
)
